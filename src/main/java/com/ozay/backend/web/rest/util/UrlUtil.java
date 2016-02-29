package com.ozay.backend.web.rest.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by naofumiezaki on 11/21/15.
 */
public class UrlUtil {
    public static String baseUrlGenerator(HttpServletRequest request){
        String baseUrl = request.getScheme() +
            "://" +
            request.getServerName() +
            ":" +
            request.getServerPort();
        return baseUrl;
    }
}
