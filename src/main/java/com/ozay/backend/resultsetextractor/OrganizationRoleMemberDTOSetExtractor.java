package com.ozay.backend.resultsetextractor;

import com.ozay.backend.web.rest.dto.OrganizationRoleMemberDTO;
import com.ozay.backend.web.rest.dto.OrganizationUserDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naofumiezaki on 11/17/15.
 */
public class OrganizationRoleMemberDTOSetExtractor implements ResultSetExtractor {
    @Override
    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<OrganizationRoleMemberDTO> organizationUserDTOs = new ArrayList<OrganizationRoleMemberDTO>();
        while(resultSet.next()){
            OrganizationRoleMemberDTO organizationUserDTO = new OrganizationRoleMemberDTO();
            organizationUserDTO.setOrganizationUserId(resultSet.getLong("organizationUserId"));
            organizationUserDTO.setRoleId(resultSet.getLong("roleId"));
            organizationUserDTOs.add(organizationUserDTO);
        }
        return organizationUserDTOs;
    }
}
