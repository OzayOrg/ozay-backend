package com.ozay.backend.repository;

import com.ozay.backend.model.AccountInformation;
import com.ozay.backend.model.Building;
import com.ozay.backend.resultsetextractor.AccountSimpleResultSetExtractor;
import com.ozay.backend.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naofumiezaki on 11/1/15.
 */
@Repository
public class AccountRepository {

    private final Logger log = LoggerFactory.getLogger(AccountRepository.class);
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Inject
    private BuildingRepository buildingRepository;

    @Inject
    private OrganizationUserRepository organizationUserRepository;


    public AccountInformation getLoginUserInformation(){
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", SecurityUtils.getCurrentLogin());
        String query = "SELECT s.id as s_id, s.user_id as s_user_id, ou.organization_id as organization_id from jhi_user u LEFT JOIN subscription s ON u.id = s.user_id AND (s.date_from <= now() AND s.date_to > now() ) LEFT JOIN organization_user ou ON u.id = ou.user_id WHERE u.login =:login AND (s.user_id IS NOT NULL OR ou.user_id IS NOT NULL)";

        List<AccountInformation> accountInformationList = (List<AccountInformation>)namedParameterJdbcTemplate.query(query, params, new AccountSimpleResultSetExtractor());
        AccountInformation accountInformation = null;
        if(accountInformationList.size() > 0){
            accountInformation = accountInformationList.get(0);
        }

        return accountInformation;
    }

    public boolean isSubscriber(Long buildingId){
        String query3 = "SELECT COUNT(*) FROM jhi_user u INNER JOIN subscription s ON s.user_id = u.id AND s.date_from <= now() and s.date_to > now() INNER JOIN organization o ON o.user_id = u.id INNER JOIN building b ON b.organization_id = o.id AND b.id = :buildingId WHERE u.login = :login";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", SecurityUtils.getCurrentLogin());
        params.addValue("buildingId", buildingId);

        Long countForBuildingSubscriber  = namedParameterJdbcTemplate.queryForObject(query3, params, Long.class);
        if(countForBuildingSubscriber > 0){
            return true;
        } else{
            return false;
        }
    }
    public boolean isOrganizationSubscriber(Long organizationId){
        String query4 = "SELECT COUNT(*) FROM jhi_user u INNER JOIN subscription s ON s.user_id = u.id AND s.date_from <= now() and s.date_to > now() INNER JOIN organization o ON o.user_id = u.id AND o.id=:organizationId WHERE u.login = :login";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", SecurityUtils.getCurrentLogin());
        params.addValue("organizationId", organizationId);

        Long countForOrganizationSubscriber  = namedParameterJdbcTemplate.queryForObject(query4, params, Long.class);
        System.out.println(countForOrganizationSubscriber);
        if(countForOrganizationSubscriber > 0){
            return true;
        } else {
            return false;
        }
    }

    public AccountInformation getLoginUserInformation(Long buildingId, Long organizationId){

        if(organizationId == null){
            Building building = buildingRepository.findOne(buildingId);
            organizationId = building.getOrganizationId();
        }

        String query = "SELECT DISTINCT p.key " +
            "FROM jhi_user u " +
            "LEFT JOIN member m ON u.id =  m.user_id AND m.building_id = :buildingId " +
            "LEFT JOIN role_member rm ON rm.member_id = m.id " +
            "LEFT JOIN role_permission rp ON rp.role_id = rm.role_id " +
            "LEFT JOIN permission p ON p.id = rp.permission_id " +
            "WHERE u.login = :login";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", SecurityUtils.getCurrentLogin());
        params.addValue("buildingId", buildingId);

        List<String> list1 = namedParameterJdbcTemplate.queryForList(query, params, String.class);
        log.debug("list1 {}", list1);


        String query2 = "SELECT DISTINCT p.key " +
            "FROM jhi_user u " +
            "LEFT JOIN organization_user ou ON ou.user_id = u.id AND ou.organization_id=:organizationId " +
            "LEFT JOIN organization_user_permission oup ON ou.id = oup.organization_user_id " +
            "LEFT JOIN permission p ON p.id = oup.permission_id " +
            "WHERE u.login = :login";

        params = new MapSqlParameterSource();
        params.addValue("login", SecurityUtils.getCurrentLogin());
        params.addValue("organizationId", organizationId);

        List<String> list2 = namedParameterJdbcTemplate.queryForList(query2, params, String.class);
        log.debug("list2 {}", list2);


        AccountInformation accountInformation = new AccountInformation();
        accountInformation.setAuthorities(new ArrayList<String>());
        boolean organizationHasAccess = false;

        if(list1 != null){
            for(String s1 : list1){
                if(s1 != null){
                    accountInformation.getAuthorities().add(s1);
                }
            }
        }

        if(list2 != null){
            for(String s2 : list2){
                if(s2 != null){
                    if(organizationHasAccess == false){
                        organizationHasAccess = true;
                        accountInformation.getAuthorities().add("ORGANIZATION_HAS_ACCESS");
                    }
                    accountInformation.getAuthorities().add(s2);
                }
            }
        }

        if(organizationHasAccess == false){
            boolean isOrganizationUser = organizationUserRepository.isOrganizationUser(organizationId);
            if(isOrganizationUser == true){
                organizationHasAccess = true;
                accountInformation.getAuthorities().add("ORGANIZATION_HAS_ACCESS");
            }
        }


        if(this.isSubscriber(buildingId) == true){
            accountInformation.getAuthorities().add("ROLE_SUBSCRIBER");
            if(organizationHasAccess == false){
                organizationHasAccess = true;
                accountInformation.getAuthorities().add("ORGANIZATION_HAS_ACCESS");
            }
        }

        if(this.isOrganizationSubscriber(organizationId)){
            accountInformation.getAuthorities().add("ROLE_ORGANIZATION_SUBSCRIBER");
            if(organizationHasAccess == false){
                organizationHasAccess = true;
                accountInformation.getAuthorities().add("ORGANIZATION_HAS_ACCESS");
            }
        }


        System.out.println(accountInformation);
        return accountInformation;
    }
}
