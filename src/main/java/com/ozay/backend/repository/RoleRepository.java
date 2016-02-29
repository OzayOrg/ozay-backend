package com.ozay.backend.repository;

import com.ozay.backend.model.Role;
import com.ozay.backend.resultsetextractor.RoleRolePermissionSetExtractor;
import com.ozay.backend.resultsetextractor.RoleSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * Created by naofumiezaki on 11/1/15.
 */
@Repository
public class RoleRepository {
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Role> findAllByBuildingId(Long buildingId){
        String query = "SELECT * FROM ROLE WHERE building_id = :buildingId ORDER BY sort_order";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", buildingId);
        return (List<Role>)namedParameterJdbcTemplate.query(query, params, new RoleSetExtractor(){});
    }

    public List<Role> findAllNonOrganizationRolesByBuildingId(Long buildingId){
        String query = "SELECT * FROM ROLE WHERE building_id = :buildingId AND organization_user_role = false ORDER BY sort_order";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", buildingId);
        return (List<Role>)namedParameterJdbcTemplate.query(query, params, new RoleSetExtractor(){});
    }

    public List<Role> findAllExceptId(Long buildingId, Long roleId){
        String query = "SELECT * FROM ROLE WHERE building_id = :buildingId AND id <> :roleId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", buildingId);
        params.addValue("roleId", roleId);
        return (List<Role>)namedParameterJdbcTemplate.query(query, params, new RoleSetExtractor(){});
    }

    public Role findOne(Long id){
        String query = "SELECT r.*, rp.role_id, permission_id  FROM role r LEFT JOIN role_permission rp ON rp.role_id = r.id WHERE r.id = :roleId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", id);
        List<Role> roles = (List<Role>)namedParameterJdbcTemplate.query(query, params, new RoleRolePermissionSetExtractor(){});
        if(roles.size() == 1){
            System.out.println(roles.get(0));
            return roles.get(0);
        } else {
            System.out.println(123);
            return null;
        }
    }

    public void create(Role role){
        String query="INSERT INTO role (building_id, name, organization_user_role, belong_to, sort_order) VALUES(:buildingId, :name, :organizationUserRole, :belongTo, (SELECT Max(sort_order) + 1 FROM role WHERE building_id = :buildingId)) RETURNING id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", role.getBuildingId());
        params.addValue("name", role.getName());
        params.addValue("organizationUserRole", role.isOrganizationUserRole());
        params.addValue("belongTo", role.getBelongTo());
        Long id = namedParameterJdbcTemplate.queryForObject(query, params, Long.class );
        role.setId(id);
    }

    public void update(Role role){
        String query="UPDATE role SET building_id=:buildingId, name=:name, sort_order=:sortOrder, belong_to=:belongTo, organization_user_role= :organizationUserRole WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", role.getBuildingId());
        params.addValue("name", role.getName());
        params.addValue("sortOrder", role.getSortOrder());
        params.addValue("organizationUserRole", role.isOrganizationUserRole());
        params.addValue("id", role.getId());
        params.addValue("belongTo", role.getBelongTo());
        namedParameterJdbcTemplate.update(query, params);
    }

    public void delete(Role role){
        String query = "DELETE FROM role WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", role.getId());
        namedParameterJdbcTemplate.update(query, params);
    }

    public void deleteByIds(Set<Long> ids){
        String query = "DELETE FROM role WHERE id IN (:ids)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);
        namedParameterJdbcTemplate.update(query, params);
    }
}
