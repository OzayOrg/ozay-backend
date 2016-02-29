package com.ozay.backend.repository;

import com.ozay.backend.model.Member;
import com.ozay.backend.model.Role;
import com.ozay.backend.resultsetextractor.MemberSetExtractor;
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
public class MemberRepository {
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String MemberSetExtractorQuery = "SELECT m.*, u.email as user_email, r.id as role_id, r.name as role_name, r.belong_to as role_belong_to, r.sort_order as role_sort_order from member m LEFT JOIN jhi_user u ON m.user_id = u.id LEFT JOIN role_member rm ON m.id = rm.member_id LEFT JOIN role r ON r.id = rm.role_id ";

    public List<Member> findAllByBuildingId(Long buildingId){
        String query = this.MemberSetExtractorQuery + "WHERE m.building_id = :buildingId AND m.deleted= false ORDER BY m.id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", buildingId);
        return (List<Member>)namedParameterJdbcTemplate.query(query, params, new MemberSetExtractor());
    }

    public Member findOne(Long id){
        String query = this.MemberSetExtractorQuery + "WHERE m.deleted= false AND m.id=:id ORDER BY m.id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        List<Member> members =  (List<Member>)namedParameterJdbcTemplate.query(query, params, new MemberSetExtractor());
        if(members.size() == 1){
            return members.get(0);
        } else {
            return null;
        }
    }

    public List<Member> findAllByIds(Set<Long> ids){
        String query = this.MemberSetExtractorQuery + "WHERE m.id IN (:ids) AND m.deleted=false ORDER BY m.id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);
        return (List<Member>)namedParameterJdbcTemplate.query(query, params, new MemberSetExtractor());
    }

    public Member findOneByUserId(Long userId){
        String query = this.MemberSetExtractorQuery + "WHERE user_id = :userId AND m.deleted = false";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        List<Member> members = (List<Member>)namedParameterJdbcTemplate.query(query, params, new MemberSetExtractor());
        if(members.size() == 1){
            return members.get(0);
        }else{
            return null;
        }
    }

    public List<Member> findAllByRole(Role role){
        String query = this.MemberSetExtractorQuery + "WHERE rm.role_id = :roleId AND m.deleted= false ORDER BY m.id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", role.getId());
        return (List<Member>)namedParameterJdbcTemplate.query(query, params, new MemberSetExtractor());
    }



    public Member findOneByOrganizationUserId(Long organizationUserId, Long buildingId){
        String query = this.MemberSetExtractorQuery + "WHERE organization_user_id = :organizationUserId AND m.building_id =:buildingId AND m.deleted = false";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationUserId", organizationUserId);
        params.addValue("buildingId", buildingId);
        List<Member> members = (List<Member>)namedParameterJdbcTemplate.query(query, params, new MemberSetExtractor());
        if(members.size() == 1){
            return members.get(0);
        }else{
            return null;
        }
    }

    public List<Member> searchMembers(Long buildingId, String[] items){
        String query =this.MemberSetExtractorQuery + "WHERE deleted = false AND m.building_id =:buildingId ";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", buildingId);

        String queryForList = "";
        for(int i = 0; i< items.length;i++){
            String param = "param" + i;
            params.addValue(param, items[i]);
            if(i != 0){
                queryForList += " OR ";
            }
            queryForList += " LOWER(m.first_name) LIKE :" + param +
                " OR LOWER(m.last_name) LIKE :" + param +
                " OR LOWER(m.phone) LIKE :" + param +
                " OR LOWER(m.email) LIKE :" + param +
                " OR LOWER(m.unit) LIKE :" + param +
                " OR LOWER(m.parking) LIKE :" + param;
        }
        if(items.length > 0){
            query += " AND (";
            query += queryForList;
            query += ")";
        }


        return (List<Member>)namedParameterJdbcTemplate.query(query, params, new MemberSetExtractor());
    }

    public void create(Member member){
        String insert = "INSERT INTO member(user_id, first_name, last_name, email, phone, building_id, ownership, unit, parking, organization_user_id) VALUES (:userId, :firstName, :lastName, :email, :phone, :buildingId, :ownership, :unit, :parking, :organizationUserId) RETURNING id";
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("userId", member.getUserId());
        params.addValue("firstName", member.getFirstName());
        params.addValue("lastName", member.getLastName());
        params.addValue("email", member.getEmail());
        params.addValue("phone", member.getPhone());
        params.addValue("unit", member.getUnit());
        params.addValue("buildingId", member.getBuildingId());
        params.addValue("ownership", member.getOwnership());
        params.addValue("parking", member.getParking());
        params.addValue("organizationUserId", member.getOrganizationUserId());

        Long id = namedParameterJdbcTemplate.queryForObject(insert, params, Long.class);
        member.setId(id);
    }

    public void updateUserIdByOrganizationId(Long userId, Long organizationId){
        String query = "UPDATE member SET user_id=:userId WHERE organization_user_id = :organizationId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("organizationId", organizationId);
        namedParameterJdbcTemplate.update(query, params);
    }

    public void update(Member member){
        String query = "UPDATE member SET user_id=:userId, first_name=:firstName, last_name=:lastName, email=:email, unit=:unit, ownership=:ownership, parking =:parking, deleted=:deleted WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", member.getId());
        params.addValue("userId", member.getUserId());
        params.addValue("firstName", member.getFirstName());
        params.addValue("lastName", member.getLastName());
        params.addValue("email", member.getEmail());
        params.addValue("unit", member.getUnit());
        params.addValue("ownership", member.getOwnership());
        params.addValue("parking", member.getParking());
        params.addValue("deleted", member.isDeleted());
        namedParameterJdbcTemplate.update(query, params);
    }

    public void softDelete(Member member){
        String query = "UPDATE member SET deleted=:deleted WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", member.getId());
        params.addValue("deleted", true);
        namedParameterJdbcTemplate.update(query, params);
    }
}
