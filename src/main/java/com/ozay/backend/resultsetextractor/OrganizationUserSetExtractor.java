package com.ozay.backend.resultsetextractor;

import com.ozay.backend.model.OrganizationUser;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naofumiezaki on 11/17/15.
 */
public class OrganizationUserSetExtractor implements ResultSetExtractor {
    @Override
    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<OrganizationUser> organizationUsers = new ArrayList<OrganizationUser>();
        while(resultSet.next()){
            OrganizationUser organizationUser = new OrganizationUser();
            organizationUser.setId(resultSet.getLong("id"));
            organizationUser.setUserId(resultSet.getLong("user_id"));
            organizationUser.setTempUserId(resultSet.getLong("temp_user_id"));
            organizationUser.setOrganizationId(resultSet.getLong("organization_id"));
            organizationUser.setActivated(resultSet.getBoolean("activated"));
            organizationUsers.add(organizationUser);
        }

        return organizationUsers;
    }
}
