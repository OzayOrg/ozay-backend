package com.ozay.backend.repository;

import com.ozay.backend.model.InvitedMember;
import com.ozay.backend.resultsetextractor.InvitedMemberSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by naofumiezaki on 10/30/15.
 */

@Repository
public class InvitedMemberRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public InvitedMember findOneByActivationKey(String activationKey){
        String query = "SELECT * FROM invited_member WHERE activation_key = :activation_key and activated = false";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("activation_key", activationKey);
        List<InvitedMember> invitedMembers = (List<InvitedMember>)namedParameterJdbcTemplate.query(query, params, new InvitedMemberSetExtractor());
        if(invitedMembers.size() == 1){
            return invitedMembers.get(0);
        }else {
            return null;
        }
    }

    public void create(InvitedMember invitedMember){
        String query = "INSERT INTO invited_member" +
            "(member_id, lang_key, activation_key, created_by) "
            + "VALUES(:member_id, :lang_key, :activation_key, :created_by)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("member_id", invitedMember.getMemberId());
        params.addValue("lang_key", invitedMember.getLangKey());
        params.addValue("activation_key", invitedMember.getActivationKey());
        params.addValue("created_by", invitedMember.getCreatedBy());

        namedParameterJdbcTemplate.update(query, params);
    }

    public void update(InvitedMember invitedMember){ // activate
        String query = "UPDATE invited_member SET activated=:activated, activated_date = now() WHERE activation_key=:activationKey";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("activated", invitedMember.isActivated());
        params.addValue("activationKey", invitedMember.getActivationKey());
        namedParameterJdbcTemplate.update(query, params);
    }

}

