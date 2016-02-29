package com.ozay.backend.resultsetextractor;

import com.ozay.backend.model.Role;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naofumiezaki on 11/17/15.
 */
public class RoleSetExtractor implements ResultSetExtractor {
    @Override
    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Role> roles = new ArrayList<Role>();
        while(resultSet.next()){
            Role role = new Role();
            role.setId(resultSet.getLong("id"));
            role.setName(resultSet.getString("name"));
            role.setBelongTo(resultSet.getLong("belong_to"));
            role.setOrganizationUserRole((resultSet.getBoolean("organization_user_role")));
            role.setBuildingId(resultSet.getLong("building_id"));
            role.setSortOrder(resultSet.getLong("sort_order"));

            roles.add(role);
        }
        return roles;
    }
}
