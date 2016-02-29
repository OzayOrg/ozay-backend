package com.ozay.backend.repository;

import com.ozay.backend.model.OrganizationUser;
import com.ozay.backend.resultsetextractor.OrganizationUserSetExtractor;
import com.ozay.backend.security.SecurityUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by naofumiezaki on 11/1/15.
 */
@Repository
public class OrganizationUserRepository {
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrganizationUser findOne(Long id){
        String query = "SELECT * FROM organization_user WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        List <OrganizationUser> organizationUsers = (List<OrganizationUser>)namedParameterJdbcTemplate.query(query, params, new OrganizationUserSetExtractor());
        if(organizationUsers.size() == 1){
            return organizationUsers.get(0);
        } else {
            return null;
        }
    }

    public boolean isOrganizationUser(Long organizationId){
        String query = "SELECT COUNT(*) FROM organization_user WHERE user_id=(SELECT id from jhi_user WHERE login=:login) AND organization_id=:organizationId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", SecurityUtils.getCurrentLogin());
        params.addValue("organizationId", organizationId);
        Long count = namedParameterJdbcTemplate.queryForObject(query, params, Long.class);
        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

    public OrganizationUser findOneByTempUserId(Long tempUserId){
        String query = "SELECT * FROM organization_user WHERE temp_user_id=:tempUserId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("tempUserId", tempUserId);
        List <OrganizationUser> organizationUsers = (List<OrganizationUser>)namedParameterJdbcTemplate.query(query, params, new OrganizationUserSetExtractor());
        if(organizationUsers.size() == 1){
            return organizationUsers.get(0);
        } else {
            return null;
        }
    }

    public Long countAllNonExistingUserByOrganizationIdAndEmail(Long organizationId, String email){
        String query = "SELECT * FROM temp_user iu INNER JOIN organization_user ou ON iu.id = ou.user_id AND ou.activated = false WHERE ou.organization_id = :organizationId AND email=:email AND iu.activated = false";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationId", organizationId);
        params.addValue("email", email);
        return namedParameterJdbcTemplate.queryForObject(query, params, Long.class);
    }

    public void create(OrganizationUser organizationUser){
        String query = "INSERT INTO organization_user (user_id, temp_user_id, organization_id, activated) VALUES(:userId, :tempUserId, :organizationId, :activated) RETURNING id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", organizationUser.getUserId());
        params.addValue("tempUserId", organizationUser.getTempUserId());
        params.addValue("organizationId", organizationUser.getOrganizationId());
        params.addValue("activated", organizationUser.isActivated());
        Long id = namedParameterJdbcTemplate.queryForObject(query,params, Long.class);
        organizationUser.setId(id);
    }

    public void update(OrganizationUser organizationUser){
        String query = "UPDATE organization_user SET user_id=:userId, activated = :activated WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", organizationUser.getUserId());
        params.addValue("id", organizationUser.getId());
        params.addValue("activated", organizationUser.isActivated());
        namedParameterJdbcTemplate.update(query,params);
    }

    public void deleteAllByUser(Long id){
        String query="DELETE FROM organization_user WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        namedParameterJdbcTemplate.update(query,params);
    }
}
