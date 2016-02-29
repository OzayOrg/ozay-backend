package com.ozay.backend.repository;

import com.ozay.backend.model.OrganizationUserPermission;
import com.ozay.backend.resultsetextractor.OrganizationUserPermissionSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by naofumiezaki on 11/20/15.
 */
@Repository
public class OrganizationUserPermissionRepository {
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<OrganizationUserPermission> findAll(Long organizationUserId){
        String query = "SELECT * FROM organization_user_permission WHERE organization_user_id = :organizationUserId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationUserId", organizationUserId);
        return (List<OrganizationUserPermission>)namedParameterJdbcTemplate.query(query, params, new OrganizationUserPermissionSetExtractor());
    }

    public void create(OrganizationUserPermission organizationUserPermission){
        String query = "INSERT INTO organization_user_permission (organization_user_id, permission_id) VALUES(:organizationUserId, :permissionId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationUserId", organizationUserPermission.getOrganizationUserId());
        params.addValue("permissionId", organizationUserPermission.getPermissionId());
        namedParameterJdbcTemplate.update(query,params);
    }

    public void deleteAllByOrganizationUserId(Long organizationUserId){
        String query="DELETE FROM organization_user_permission WHERE organization_user_id=:organizationUserId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationUserId", organizationUserId);
        namedParameterJdbcTemplate.update(query,params);
    }
}
