package com.ozay.backend.resultsetextractor;

import com.ozay.backend.model.Role;
import com.ozay.backend.model.RolePermission;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by naofumiezaki on 11/17/15.
 */
public class RoleRolePermissionSetExtractor implements ResultSetExtractor {
    @Override
    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Role> roles = new ArrayList<Role>();
        Role role = null;
        Set<RolePermission> rolePermissions = new HashSet<>();
        while(resultSet.next()){
            if(role == null){
                role = new Role();
                role.setId(resultSet.getLong("id"));
                role.setName(resultSet.getString("name"));
                role.setBelongTo(resultSet.getLong("belong_to"));
                role.setSortOrder(resultSet.getLong("sort_order"));
                role.setOrganizationUserRole((resultSet.getBoolean("organization_user_role")));
                role.setBuildingId(resultSet.getLong("building_id"));
            }
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(resultSet.getLong("role_id"));
            rolePermission.setPermissionId(resultSet.getLong("permission_id"));
            if(rolePermission.getRoleId() != 0 && rolePermission.getPermissionId() != 0){
                rolePermissions.add(rolePermission);
            }
        }
        role.setRolePermissions(rolePermissions);
        roles.add(role);
        return roles;
    }
}
