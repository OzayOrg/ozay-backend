package com.ozay.backend.repository;

import com.ozay.backend.model.Permission;
import com.ozay.backend.resultsetextractor.PermissionSetExtractor;
import com.ozay.backend.security.SecurityUtils;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by naofumiezaki on 11/17/15.
 */
@Repository
public class PermissionRepository {
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final int MEMBER_TYPE = 1;
    private static final int ORGANIZATION_TYPE = 2;

    public boolean validateMemberInterceptor(Long buildingId, String key){
        String query="SELECT COUNT(*) FROM jhi_user u INNER JOIN member m ON u.id = m.user_id AND m.deleted = false INNER JOIN role_member rm ON m.id = rm.member_id  INNER JOIN role_permission rp ON rm.role_id = rp.role_id INNER JOIN permission p ON p.id = rp.permission_id WHERE m.building_id = :buildingId AND p.key = :key AND u.login=:login";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", buildingId);
        params.addValue("key", key);
        params.addValue("login", SecurityUtils.getCurrentLogin());
        Long count = namedParameterJdbcTemplate.queryForObject(query, params, Long.class);
        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

    public boolean validateOrganizationInterceptor(Long organizationId, String key){
        String query="SELECT COUNT(*) FROM jhi_user u INNER JOIN organization_user ou ON user_id = u.id AND ou.organization_id = :organizationId INNER JOIN organization_user_permission oup ON oup.organization_user_id = ou.id INNER JOIN permission p ON p.id = oup.permission_id WHERE u.login=:login AND p.key = :key";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationId", organizationId);
        params.addValue("key", key);
        params.addValue("login", SecurityUtils.getCurrentLogin());
        Long count = namedParameterJdbcTemplate.queryForObject(query, params, Long.class);
        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

    public List<Permission> findOrganizationPermissions(){
        String query="SELECT * FROM permission WHERE type=:type ORDER BY sort_order";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("type", this.ORGANIZATION_TYPE);
        return (List<Permission>)namedParameterJdbcTemplate.query(query, params, new PermissionSetExtractor(){});
    }

    public List<Permission> findRolePermissions(){
        String query="SELECT * FROM permission WHERE type=:type ORDER BY sort_order";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("type", this.MEMBER_TYPE);
        return (List<Permission>)namedParameterJdbcTemplate.query(query, params, new PermissionSetExtractor(){});
    }
}
