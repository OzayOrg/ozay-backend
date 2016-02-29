package com.ozay.backend.resultsetextractor;

import com.ozay.backend.model.Building;
import com.ozay.backend.model.Member;
import com.ozay.backend.model.Role;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by naofumiezaki on 10/30/15.
 */
public class MemberSetExtractor implements ResultSetExtractor {
    @Override
    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Member> list = new ArrayList<Member>();
        Member member = null;
        Long previous = null;

        while(resultSet.next()){
            if(previous == null || previous != resultSet.getLong("id")) {
                if (previous != null) {
                    list.add(member);
                }
            }
            previous = resultSet.getLong("id");
            member = new Member();
            member.setRoles(new HashSet<>());
            member.setId(resultSet.getLong("id"));
            member.setFirstName(resultSet.getString("first_name"));
            member.setLastName(resultSet.getString("last_name"));
            member.setUnit(resultSet.getString("unit"));
            member.setPhone(resultSet.getString("phone"));

            member.setBuildingId(resultSet.getLong("building_id"));
            member.setUserId(resultSet.getLong("user_id"));
            member.setOrganizationUserId(resultSet.getLong("organization_user_id"));
            member.setDeleted(resultSet.getBoolean("deleted"));

            member.setEmail(resultSet.getString("user_email"));
            if(member.getEmail() == null){
                member.setEmail(resultSet.getString("email"));
            }

            Role role = new Role();
            role.setId(resultSet.getLong("role_id"));
            role.setBuildingId(resultSet.getLong("building_id"));
            role.setName(resultSet.getString("role_name"));
            role.setSortOrder(resultSet.getLong("role_sort_order"));
            role.setBelongTo(resultSet.getLong("role_belong_to"));
            if(role.getId() != 0){
                member.getRoles().add(role);
            }
        }
        if(member != null){
            list.add(member);
        }
        return list;
    }

}
