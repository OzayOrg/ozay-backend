package com.ozay.backend.resultsetextractor;

import com.ozay.backend.model.Permission;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naofumiezaki on 11/17/15.
 */
public class PermissionSetExtractor implements ResultSetExtractor {
    @Override
    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Permission> permissions = new ArrayList<Permission>();
        while(resultSet.next()){
            Permission permission = new Permission();
            permission.setId(resultSet.getLong("id"));
            permission.setLabel(resultSet.getString("label"));
            permission.setKey(resultSet.getString("key"));
            permissions.add(permission);
        }

        return permissions;
    }
}
