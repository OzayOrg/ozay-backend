package com.ozay.backend.resultsetextractor;

import com.ozay.backend.model.InvitedMember;
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
public class InvitedMemberSetExtractor implements ResultSetExtractor {
    @Override
    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<InvitedMember> list = new ArrayList<InvitedMember>();


        while(resultSet.next()) {
            InvitedMember invitedMember = new InvitedMember();
            invitedMember.setId(resultSet.getLong("id"));
            invitedMember.setMemberId(resultSet.getLong("member_id"));
            invitedMember.setActivationKey(resultSet.getString("activation_key"));
            invitedMember.setActivated(resultSet.getBoolean("activated"));
            invitedMember.setLangKey(resultSet.getString("lang_key"));
            list.add(invitedMember);
        }
        return list;
    }

}
