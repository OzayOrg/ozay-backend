package com.ozay.backend.repository;

import com.ozay.backend.domain.User;
import com.ozay.backend.model.Organization;
import com.ozay.backend.resultsetextractor.OrganizationSetExtractor;
import com.ozay.backend.resultsetextractor.OrganizationUserDTOSetExtractor;
import com.ozay.backend.security.SecurityUtils;
import com.ozay.backend.web.rest.dto.OrganizationUserDTO;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by naofumiezaki on 11/1/15.
 */
@Repository
public class OrganizationRepository {
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Organization> findAllUserCanAccess(User user){
        String query = "SELECT DISTINCT ON (o.id) * FROM organization o LEFT JOIN organization_user ou ON o.id = ou.organization_id WHERE o.user_id = :userId OR ou.user_id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", user.getId());
        return (List<Organization>) namedParameterJdbcTemplate.query(query, params, new OrganizationSetExtractor());
    }

    public List<OrganizationUserDTO> findAllOrganizationActivatedUser(Long organizationId){
        String query = "SELECT u.*, ou.id as organization_user_id, ou.id as user_id, ou.activated as activated FROM jhi_user u INNER JOIN organization_user ou ON u.id = ou.user_id AND ou.activated = true WHERE ou.organization_id = :organizationId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationId", organizationId);
        return (List<OrganizationUserDTO>) namedParameterJdbcTemplate.query(query, params, new OrganizationUserDTOSetExtractor());
    }

    public List<OrganizationUserDTO> findAllOrganizationInactivatedUser(Long organizationId){
        String query = "SELECT u.*, ou.id as organization_user_id, ou.id as user_id, ou.activated as activated FROM temp_user u INNER JOIN organization_user ou ON u.id = ou.temp_user_id AND ou.activated = false WHERE ou.organization_id = :organizationId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationId", organizationId);
        return (List<OrganizationUserDTO>) namedParameterJdbcTemplate.query(query, params, new OrganizationUserDTOSetExtractor());
    }

    public Organization findOne(long organizationId){
        String query = "SELECT * FROM organization WHERE id = :organizationId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationId", organizationId);
        List<Organization> organizations = (List<Organization>) namedParameterJdbcTemplate.query(query, params, new OrganizationSetExtractor());
        if(organizations.size() == 1){
            return organizations.get(0);
        } else {
            return null;
        }
    }

    public void create(Organization organization){
        String query = "INSERT INTO organization (user_id, name, created_date, street, apartment, city, phone, state, country, zip, created_by) " +
            "VALUES(:userId, :name, now(), :street, :apartment, :city, :phone, :state, :country, :zip, :createdBy" +
            ")";

        SecurityUtils.getCurrentLogin();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", organization.getUserId());
        params.addValue("name", organization.getName());
        params.addValue("street", organization.getStreet());
        params.addValue("apartment", organization.getApartment());
        params.addValue("city", organization.getCity());
        params.addValue("phone", organization.getPhone());
        params.addValue("state", organization.getState());
        params.addValue("country", organization.getCountry());
        params.addValue("zip", organization.getZip());
        params.addValue("createdBy", organization.getCreatedBy());
        namedParameterJdbcTemplate.update(query,params);
    }

    public void update(Organization organization){
        String query = "UPDATE organization " +
            "SET user_id=:userId, " +
            "name=:name, " +
            "street=:street," +
            "apartment=:apartment," +
            "city=:city," +
            "phone=:phone," +
            "state=:state," +
            "country=:country," +
            "zip=:zip, " +
            "modified_date=now(), " +
            "modified_by=:modifiedBy " +
            "WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", organization.getUserId());
        params.addValue("name", organization.getName());
        params.addValue("street", organization.getStreet());
        params.addValue("apartment", organization.getApartment());
        params.addValue("city", organization.getCity());
        params.addValue("phone", organization.getPhone());
        params.addValue("state", organization.getState());
        params.addValue("country", organization.getCountry());
        params.addValue("zip", organization.getZip());
        params.addValue("modifiedBy", organization.getModifiedBy());
        params.addValue("id", organization.getId());
        namedParameterJdbcTemplate.update(query,params);
    }

}
