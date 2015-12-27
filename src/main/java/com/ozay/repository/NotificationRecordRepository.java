package com.ozay.repository;

import com.ozay.model.NotificationRecord;
import com.ozay.rowmapper.NotificationRecordMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.ozay.resultsetextractor.NotificationTrackResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by naofumiezaki on 6/9/15.
 */
@Repository
public class NotificationRecordRepository {
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<NotificationRecord> getAllByNotificationId(Long notificationId){
        String query = "SELECT nr.*, m.first_name, m.last_name, m.unit FROM notification_record nr INNER JOIN member m ON nr.member_id = m.id WHERE nr.notification_id = :notificationId";

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("notificationId", notificationId);

        return namedParameterJdbcTemplate.query(query, params, new NotificationRecordMapper());
    }

    public List<NotificationRecord> findAllTrackedByBuildingId(Long buildingId){
        //int limit = 20;
        //offset = offset * limit;
        String query = "SELECT n.created_date, n.subject, nr.*, m.first_name, m.last_name, m.unit, n.track FROM notification_record nr INNER JOIN notification n ON nr.notification_id = n.id INNER JOIN member m ON nr.member_id = m.id where n.building_id = :buildingId ORDER BY nr.track_complete, n.created_date DESC";

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("buildingId", buildingId);
        //params.addValue("limit", limit);
        //params.addValue("offset", offset);

        return (List<NotificationRecord>)namedParameterJdbcTemplate.query(query, params, new NotificationTrackResultSetExtractor());
    }

    public void create(NotificationRecord notificationRecord){
        String query = "INSERT INTO notification_record (notification_id, member_id, success, email, note) VALUES(:notificationId, :memberId, :success, :email, :note)";
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("notificationId", notificationRecord.getNotificationId());
        params.addValue("memberId", notificationRecord.getMemberId());
        params.addValue("success", notificationRecord.isSuccess());
        params.addValue("note", notificationRecord.getNote());
        params.addValue("email", notificationRecord.getEmail());

        namedParameterJdbcTemplate.update(query, params);
    }
}
