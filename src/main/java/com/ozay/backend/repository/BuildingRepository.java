package com.ozay.backend.repository;

import com.ozay.backend.domain.User;
import com.ozay.backend.model.Building;
import com.ozay.backend.resultsetextractor.BuildingSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by naofumiezaki on 10/30/15.
 */

@Repository
public class BuildingRepository {
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Building> findAll(){
        String query = "SELECT b.*, o.name as organizationName FROM building b INNER JOIN organization o ON b.organization_id = o.id order by b.id";
        return (List<Building>)namedParameterJdbcTemplate.query(query, new BuildingSetExtractor(){});
    }

    public List<Building> findAllUserCanAccess(User user){
        String query = "SELECT DISTINCT ON (b.id) b.*, o.name as organizationName FROM building b LEFT JOIN organization o ON o.id = b.organization_id LEFT JOIN member m ON b.id = m.building_id AND m.deleted = false LEFT JOIN subscription s ON s.user_id = o.user_id WHERE s.user_id = :userId OR m.user_id = :userId ORDER BY b.id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", user.getId());
        return (List<Building>)namedParameterJdbcTemplate.query(query, params, new BuildingSetExtractor(){});
    }

    public List<Building> findAllOrganizationBuildings(long organizationId){
        String query = "SELECT b.*, o.name as organizationName FROM building b INNER JOIN organization o ON b.organization_id = o.id WHERE organization_id = :organizationId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationId", organizationId);
        return (List<Building>)namedParameterJdbcTemplate.query(query, params, new BuildingSetExtractor(){});
    }

    public Building findOne(long id){
        String query = "SELECT b.*, o.name as organizationName FROM building b INNER JOIN organization o ON b.organization_id = o.id WHERE b.id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        List<Building> list =  (List<Building>)namedParameterJdbcTemplate.query(query, params, new BuildingSetExtractor(){});

        if(list.size() == 1){
            return list.get(0);
        }
        else {
            return null;
        }
    }


    public void create(Building building){

        String insert = "INSERT INTO building (name, organization_id, email, street, apartment, city, state, zip, phone, total_units, created_by, created_date) VALUES (:name, :organizationId, :email, :street, :apartment, :city, :state, :zip, :phone, :totalUnits,:createdBy, now()) RETURNING id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", building.getName());
        params.addValue("organizationId", building.getOrganizationId());
        params.addValue("email", building.getEmail());
        params.addValue("street", building.getStreet());
        params.addValue("apartment", building.getApartment());
        params.addValue("city", building.getCity());
        params.addValue("state", building.getState());
        params.addValue("zip", building.getZip());
        params.addValue("phone", building.getPhone());
        params.addValue("totalUnits", building.getTotalUnits());
        params.addValue("createdBy", building.getCreatedBy());

        long id = namedParameterJdbcTemplate.queryForObject(insert, params, Integer.class );
        building.setId(id);
    }

    public void update(Building building){

        String query = "UPDATE building SET name =:name, organization_id = :organizationId, email =:email, street= :street, apartment = :apartment, city=:city, state=:state, zip=:zip, phone=:phone, total_units=:totalUnits, last_modified_by =:modifiedBy, last_modified_date=now() WHERE id=:id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", building.getName());
        params.addValue("organizationId", building.getOrganizationId());
        params.addValue("email", building.getEmail());
        params.addValue("street", building.getStreet());
        params.addValue("apartment", building.getApartment());
        params.addValue("state", building.getState());
        params.addValue("city", building.getCity());
        params.addValue("zip", building.getZip());
        params.addValue("phone", building.getPhone());
        params.addValue("totalUnits", building.getTotalUnits());
        params.addValue("modifiedBy", building.getLastModifiedBy());
        params.addValue("id", building.getId());

        namedParameterJdbcTemplate.update(query, params);
    }
}

