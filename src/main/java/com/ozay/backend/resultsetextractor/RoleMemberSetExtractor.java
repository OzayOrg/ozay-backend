package com.ozay.backend.resultsetextractor;

import com.ozay.backend.model.RoleMember;
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
public class RoleMemberSetExtractor implements ResultSetExtractor {
    @Override
    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<RoleMember> roleMembers = new ArrayList<RoleMember>();
        while(resultSet.next()){
            RoleMember roleMember = new RoleMember();
            roleMember.setMemberId(resultSet.getLong("member_id"));
            roleMember.setRoleId(resultSet.getLong("role_id"));
            roleMembers.add(roleMember);
        }
        return roleMembers;
    }
}
