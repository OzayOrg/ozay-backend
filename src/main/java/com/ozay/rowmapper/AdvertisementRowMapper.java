package com.ozay.rowmapper;

import com.ozay.model.Advertisement;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Adi Subramanian on 10/15/2015.
 */
public class AdvertisementRowMapper implements RowMapper {

    @Override
    public Advertisement mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Advertisement advertisement = new Advertisement();
        advertisement.setTargetLocation(resultSet.getString("targetLocation"));
        advertisement.setImageLink(resultSet.getString("imageLink"));
        advertisement.setCaption(resultSet.getString("caption"));
        advertisement.setPageLink(resultSet.getString("pageLink"));
        advertisement.setSrNo(resultSet.getInt("srNo"));
        advertisement.setBuildingId(resultSet.getString("buildingId"));
        return advertisement;
    }
}
