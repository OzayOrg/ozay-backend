package com.ozay.backend.repository;

import com.ozay.backend.model.RoleMember;
import com.ozay.backend.resultsetextractor.OrganizationRoleMemberDTOSetExtractor;
import com.ozay.backend.resultsetextractor.OrganizationUserDTOSetExtractor;
import com.ozay.backend.resultsetextractor.RoleMemberSetExtractor;
import com.ozay.backend.web.rest.dto.OrganizationRoleMemberDTO;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by naofumiezaki on 11/1/15.
 */
@Repository
public class RoleMemberRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<OrganizationRoleMemberDTO> findOrganizationRoleMembers(Long roleId){
        String query = "SELECT rm.role_id as roleId, m.organization_user_id as organizationUserId FROM role_member rm INNER JOIN member m ON rm.member_id = m.id WHERE rm.role_id = :roleId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", roleId);
        return (List<OrganizationRoleMemberDTO>)namedParameterJdbcTemplate.query(query, params, new OrganizationRoleMemberDTOSetExtractor());
    }

    public List<RoleMember> findRoleMembersByMemberId(Long memberId){
        String query = "SELECT * FROM role_member WHERE member_id = :memberId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("memberId", memberId);
        return (List<RoleMember>)namedParameterJdbcTemplate.query(query, params, new RoleMemberSetExtractor());
    }


    public boolean hasRole(long roleId, long memberId){
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", roleId);
        params.addValue("memberId", memberId);
        Integer count = namedParameterJdbcTemplate.queryForObject("SELECT COUNT(*) FROM role_member WHERE role_id =:roleId AND member_id=:memberId", params, Integer.class );
        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

    public boolean hasAnyRolesInBuilding(long buildingId, long memberId){
        String query = "SELECT COUNT(*) FROM role_member rm INNER JOIN role r ON rm.role_id = r.id WHERE r.building_id =:buildingId AND rm.member_id=:memberId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", buildingId);
        params.addValue("memberId", memberId);
        Integer count = namedParameterJdbcTemplate.queryForObject(query, params, Integer.class);
        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

    public void create(long roleId, long memberId){
        String query="INSERT INTO role_member (role_id, member_id) VALUES(:roleId, :memberId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", roleId);
        params.addValue("memberId", memberId);
        namedParameterJdbcTemplate.update(query,params);
    }

    public void delete(long roleId, long memberId){
        String query="DELETE FROM role_member WHERE role_id =:roleId AND member_id=:memberId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", roleId);
        params.addValue("memberId", memberId);
        namedParameterJdbcTemplate.update(query,params);
    }
    public void deleteAllByRoleId(Long roleId){
        String query="DELETE FROM role_member WHERE role_id=:roleId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", roleId);
        namedParameterJdbcTemplate.update(query,params);
    }


    public void deleteAllByMemberId(long memberId){
        String query="DELETE FROM role_member WHERE member_id=:memberId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("memberId", memberId);
        namedParameterJdbcTemplate.update(query,params);
    }
}
