package com.ozay.backend.resultsetextractor;

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
public class OrganizationUserDTOSetExtractor implements ResultSetExtractor {
    @Override
    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<OrganizationUserDTO> organizationUserDTOs = new ArrayList<OrganizationUserDTO>();
        while(resultSet.next()){
            OrganizationUserDTO organizationUserDTO = new OrganizationUserDTO();
            organizationUserDTO.setId(resultSet.getLong("organization_user_id"));
            organizationUserDTO.setUserId(resultSet.getLong("user_id"));
            organizationUserDTO.setFirstName(resultSet.getString("first_name"));
            organizationUserDTO.setLastName(resultSet.getString("last_name"));
            organizationUserDTO.setEmail(resultSet.getString("email"));
            organizationUserDTO.setActivated(resultSet.getBoolean("activated"));
            organizationUserDTOs.add(organizationUserDTO);
        }
        return organizationUserDTOs;
    }
}
