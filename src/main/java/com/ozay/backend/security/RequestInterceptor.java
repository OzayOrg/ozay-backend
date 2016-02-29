package com.ozay.backend.security;

import com.ozay.backend.repository.AccountRepository;
import com.ozay.backend.repository.PermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RequestInterceptor extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(RequestInterceptor.class);

    @Inject
    private PermissionRepository permissionRepository;

    @Inject
    private AccountRepository accountRepository;

    private Long buildingId;
    private Long organizationId;
    private String method;
    private HttpServletRequest request;

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("!!!!!INTERCEPTOR!!!!");


        if(SecurityUtils.getCurrentLogin() == null){
            System.out.println("!!!!!NOT LOGGED IN!!!!");
            return false;
        }
        else if(SecurityUtils.isUserInRole("ROLE_ADMIN")){
            System.out.println("!!!!!ADMIN!!!!");
            return true;
        }

        if(request.getServletPath().equals("/api/building") && request.getMethod().toUpperCase().equals("GET")){
            System.out.println("!!!!!Building GET return true!!!!");
            return true;
        }

        this.request = request;
        boolean result = this.validation();

        if(result == false){
            System.out.println("!!!!!Interceptor false!!!!");

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        System.out.println("!!!!!Interceptor true!!!!");
        return true;
    }

    private boolean validation(){

        if(this.parameterCheck(this.request) == false){
            return false;
        }

        this.method = this.request.getMethod().toUpperCase();
        System.out.println("Method: " + this.method);
        String path = this.request.getServletPath();
        System.out.println(path);
        String[] pathComponents = path.split("/");
        if(pathComponents.length < 2){
            return false;
        }
        for(String item : pathComponents){
            System.out.println(item);
        }
        String apiName = pathComponents[2];
        System.out.println("Api Name: " + apiName);

        if(apiName.equals("page") == true && this.method.equals("GET")){
            if(pathComponents.length < 4){
                return false;
            }
            System.out.println("Page true");
            return validatePageRequest(pathComponents[3]);
        } else {
            return this.validateRequest(apiName);
        }
    }

    // Non page requests
    private boolean validateRequest(String apiName){
        String key = apiName.toUpperCase() + "_" + this.method.toUpperCase();
        return this.checkPermission(key);
    }

    private boolean validatePageRequest(String path){
        String method = "GET";
        if(path.contains("-") == true){
            String[] splits = path.split("-");
            path = splits[0];
            if(splits[splits.length - 1].toUpperCase().equals("NEW")){
                method = "POST";
            } else if(splits[splits.length - 1].toUpperCase().equals("EDIT")){
                method = "PUT";
            }
        }
        // notification exception
        if(path.equals("notification") == true){
            if(this.request.getServletPath().equals("/api/page/notification") == true){
                return this.checkPermission("NOTIFICATION_POST");
            } else {
                return this.checkPermission("NOTIFICATION_GET");
            }
        } else if(path.equals("organization") == true) {
            return this.checkPermission("ORGANIZATION_PUT");
        } else {
            // common
            return this.checkPermission(path.toUpperCase() + "_" + method);
        }
    }

    private boolean checkPermission(String permission) {
        if(organizationId != null){
            System.out.println("!!!!!Organization key !!!!" + permission);
            return this.organizationPermissionCheck(permission);
        } else {
            System.out.println("!!!!!building Interceptor check!!!!" + permission);
            return this.memberPermissionCheck(permission);
        }
    }

    private boolean organizationPermissionCheck(String permission) {
        System.out.println("!!!!!Interceptor key!!!!" + permission);
        boolean result =  permissionRepository.validateOrganizationInterceptor(this.organizationId, permission);
        if(result == false){
            System.out.println("!!!!!Interceptor Organization false, check if subscriber!!!!");
            return this.isOrganizationSubscriber();
        }
        return result;
    }

    private boolean memberPermissionCheck( String permission) {
        System.out.println("!!!!!Interceptor key!!!!" + permission);

        boolean result =  permissionRepository.validateMemberInterceptor(this.buildingId, permission);

        if(result == false){
            System.out.println("!!!!!Interceptor Member false, check if subscriber!!!!");
            return this.isSubscriber();
        }
        return result;
    }

    private boolean isSubscriber(){
        return accountRepository.isSubscriber(this.buildingId);
    }

    private boolean isOrganizationSubscriber(){
        return accountRepository.isOrganizationSubscriber(this.organizationId);
    }

    private boolean parameterCheck(HttpServletRequest request) {
        Long buildingId = this.parseNumber(request.getParameter("building"));
        Long organizationId = this.parseNumber(request.getParameter("organization"));

        if((buildingId == null || buildingId == 0) && (organizationId == null || organizationId == 0 )){
            log.debug("False Intercepting BuildingID is null: " + request.getServletPath());
            return false;
        }
        this.buildingId = buildingId;
        this.organizationId = organizationId;
        return true;
    }

    private Long parseNumber(String value){
        Long temp = null;
        try {
            temp = Long.parseLong(value);

        } catch(Exception e){

        }
        return temp;
    }


}
