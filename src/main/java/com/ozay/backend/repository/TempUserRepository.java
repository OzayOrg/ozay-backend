package com.ozay.backend.repository;

import com.ozay.backend.model.TempUser;
import com.ozay.backend.resultsetextractor.TempUserSetExtractor;
import com.ozay.backend.security.SecurityUtils;
import com.ozay.backend.service.util.RandomUtil;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by naofumiezaki on 11/19/15.
 */
@Repository
public class TempUserRepository {
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<TempUser>findAllByOrganizationId(Long organizationId){
        String query = "SELECT * FROM temp_user WHERE organization_id = :organizationId AND activated = false";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationId", organizationId);
        return (List<TempUser>)namedParameterJdbcTemplate.query(query, params, new TempUserSetExtractor());
    }

    public TempUser findOne(Long id){
        String query = "SELECT * FROM temp_user WHERE activated = false AND id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        List<TempUser> tempUsers = (List<TempUser>)namedParameterJdbcTemplate.query(query, params, new TempUserSetExtractor());
        if(tempUsers.size() == 1){
            return tempUsers.get(0);
        } else{
            return null;
        }
    }

    public TempUser findOneByActivationKey(String activationKey){
        String query = "SELECT * FROM temp_user tu INNER JOIN organization_user ou ON tu.id = ou.temp_user_id WHERE tu.activation_key=:activationKey AND tu.activated = false";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("activationKey", activationKey);
        List<TempUser> tempUsers = (List<TempUser>)namedParameterJdbcTemplate.query(query, params, new TempUserSetExtractor());
        if(tempUsers.size() == 1){
            return tempUsers.get(0);
        } else{
            return null;
        }
    }

    public List<TempUser>findAllByOrganizationIdAndEmail(Long organizationId, String email){
        String query = "SELECT * FROM temp_user tu INNER JOIN organization_user ou ON tu.id = ou.temp_user_id AND ou.activated = false WHERE tu.activated = false AND ou.organization_id = :organizationId AND email=:email";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationId", organizationId);
        params.addValue("email", email);
        return (List<TempUser>)namedParameterJdbcTemplate.query(query, params, new TempUserSetExtractor());
    }

    public void create(TempUser tempUser){
        String query = "INSERT INTO temp_user (first_name, last_name, email, activation_key, created_by, created_date, last_modified_by, last_modified_date) VALUES (:firstName, :lastName, :email, :activationKey, :createdBy, NOW(), :createdBy, NOW()) RETURNING id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("firstName", tempUser.getFirstName());
        params.addValue("lastName", tempUser.getLastName());
        params.addValue("email", tempUser.getEmail());
        params.addValue("activationKey", RandomUtil.generateResetKey());
        params.addValue("createdBy", SecurityUtils.getCurrentLogin());
        Long id = namedParameterJdbcTemplate.queryForObject(query,params, Long.class);
        tempUser.setId(id);
    }

    public void update(TempUser tempUser){
        String query = "UPDATE temp_user SET first_name=:firstName, last_name = :lastName, email=:email, last_modified_by=:modifiedBy, last_modified_date=NOW(), activated=:activated WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("firstName", tempUser.getFirstName());
        params.addValue("lastName", tempUser.getLastName());
        params.addValue("email", tempUser.getEmail());
        params.addValue("modifiedBy", SecurityUtils.getCurrentLogin());
        params.addValue("id", tempUser.getId());
        params.addValue("activated", tempUser.isActivated());

        namedParameterJdbcTemplate.update(query,params);
    }
}
