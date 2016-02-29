package com.ozay.backend.repository;

import com.ozay.backend.model.RolePermission;
import com.ozay.backend.resultsetextractor.RolePermissionSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * Created by naofumiezaki on 11/18/15.
 */
@Repository
public class RolePermissionRepository {
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<RolePermission> findAllByRoleId(Long roleId){
        String query = "SELECT * FROM role_permission WHERE role_id = :roleId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", roleId);
        return (List<RolePermission>)namedParameterJdbcTemplate.query(query, params, new RolePermissionSetExtractor());
    }

    public void create(RolePermission rolePermission){
        String query="INSERT INTO role_permission (role_id, permission_id) VALUES(:roleId, :permissionId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", rolePermission.getRoleId());
        params.addValue("permissionId", rolePermission.getPermissionId());
        namedParameterJdbcTemplate.update(query,params);
    }

    public void deleteAllByRoleId(Long id){
        String query="DELETE FROM role_permission WHERE role_id=:roleId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", id);
        namedParameterJdbcTemplate.update(query,params);
    }

    public void deleteAllByRoleIds(Set<Long> ids){
        String query="DELETE FROM role_permission WHERE role_id IN (:ids) ";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);
        namedParameterJdbcTemplate.update(query,params);
    }
}
