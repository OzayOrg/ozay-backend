package com.ozay.backend.resultsetextractor;

import com.ozay.backend.model.OrganizationUserPermission;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naofumiezaki on 11/17/15.
 */
public class OrganizationUserPermissionSetExtractor implements ResultSetExtractor {
    @Override
    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<OrganizationUserPermission> organizationUserPermissions = new ArrayList<OrganizationUserPermission>();
        while(resultSet.next()){
            OrganizationUserPermission organizationUserPermission = new OrganizationUserPermission();
            organizationUserPermission.setOrganizationUserId(resultSet.getLong("organization_user_id"));
            organizationUserPermission.setPermissionId(resultSet.getLong("permission_id"));
            organizationUserPermissions.add(organizationUserPermission);
        }

        return organizationUserPermissions;
    }
}
