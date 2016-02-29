package com.ozay.backend.resultsetextractor;

import com.ozay.backend.model.RolePermission;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naofumiezaki on 11/17/15.
 */
public class RolePermissionSetExtractor implements ResultSetExtractor {
    @Override
    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<RolePermission> rolePermissions = new ArrayList<RolePermission>();
        while(resultSet.next()){
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(resultSet.getLong("role_id"));
            rolePermission.setPermissionId(resultSet.getLong("permission_id"));
            rolePermissions.add(rolePermission);
        }
        return rolePermissions;
    }
}
