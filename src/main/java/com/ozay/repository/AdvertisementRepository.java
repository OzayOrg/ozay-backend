package com.ozay.repository;

import com.ozay.model.Advertisement;
import com.ozay.rowmapper.AdvertisementRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;


/**
 * Created by Adi Subramanian on 10/18/2015.
 */

@Repository
public class AdvertisementRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    static int count;
    public Advertisement getAdvertisementByBuilding(int buildingId)
    {
        String query = "SELECT * FROM advertisement WHERE buildingId = :buildingId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", buildingId);
        List<Advertisement> list =  namedParameterJdbcTemplate.query(query, params, new AdvertisementRowMapper(){});
        if(!list.isEmpty())
        {
            count++;
            return list.get(count % list.size());
        }

        return null;
    }

    public int create(Advertisement advertisement)
    {
        String insert = "insert into advertisement values (:targetLocation, :caption, :imageLink, :pageLink, :srNo, :buildingName)";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("targetLocation", advertisement.getTargetLocation());
        mapSqlParameterSource.addValue("caption", advertisement.getCaption());
        mapSqlParameterSource.addValue("pageLink", advertisement.getPageLink());
        mapSqlParameterSource.addValue("imageLink", advertisement.getImageLink());
        mapSqlParameterSource.addValue("srNo", advertisement.getSrNo());
        mapSqlParameterSource.addValue("buildingId", advertisement.getBuildingId());

        int id = namedParameterJdbcTemplate.queryForObject(insert, mapSqlParameterSource, Integer.class );
        return id;
    }

}
