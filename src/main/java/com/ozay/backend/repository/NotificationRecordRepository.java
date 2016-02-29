package com.ozay.backend.repository;

import com.ozay.backend.model.Notification;
import com.ozay.backend.model.NotificationRecord;
import com.ozay.backend.model.NotificationTrack;
import com.ozay.backend.resultsetextractor.NotificationRecordResultSetExtractor;
import com.ozay.backend.resultsetextractor.NotificationTrackResultSetExtractor;
import com.ozay.backend.resultsetextractor.NotificationSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by naofumiezaki on 11/24/15.
 */
@Repository
public class NotificationRecordRepository {
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public List<NotificationRecord> findAllByNotificationId(Long notificationId){

        String query = "SELECT nr.*, m.first_name, m.last_name, m.unit, n.track FROM notification_record nr INNER JOIN member m ON nr.member_id = m.id INNER JOIN notification n ON nr.notification_id = n.id WHERE nr.notification_id = :notificationId ORDER BY n.created_date DESC";

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("notificationId", notificationId);

        return (List<NotificationRecord>)namedParameterJdbcTemplate.query(query, params, new NotificationRecordResultSetExtractor());
    }

/*    // Used for notification track
    public List<NotificationTrack> findAllByBuildingId(Long buildingId, Long offset){
        int limit = 20;
        offset = offset * limit;
        String query = "SELECT n.created_date, nr.*, m.first_name, m.last_name, m.unit, n.track FROM notification_record nr INNER JOIN notification n ON nr.notification_id = n.id AND n.track = true INNER JOIN member m ON nr.member_id = m.id ORDER BY n.created_date DESC LIMIT :limit OFFSET :offset";

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("buildingId", buildingId);
        params.addValue("limit", limit);
        params.addValue("offset", offset);

        return (List<NotificationTrack>)namedParameterJdbcTemplate.query(query, params, new NotificationTrackResultSetExtractor());
    }
 */
 //Old Notification Track using Notification.java and NotificationRecordResultSetExtractor
    public List<NotificationRecord> findAllTrackedByBuildingId(Long buildingId, Long offset){
        int limit = 20;
        offset = offset * limit;
        String query = "SELECT n.created_date, n.subject, nr.*, m.first_name, m.last_name, m.unit, n.track FROM notification_record nr INNER JOIN notification n ON nr.notification_id = n.id AND n.track = true INNER JOIN member m ON nr.member_id = m.id ORDER BY nr.track_complete, n.created_date DESC LIMIT :limit OFFSET :offset";

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("buildingId", buildingId);
        params.addValue("limit", limit);
        params.addValue("offset", offset);

        return (List<NotificationRecord>)namedParameterJdbcTemplate.query(query, params, new NotificationTrackResultSetExtractor());
    }





    public Long countAllByNotificationId(Long buildingId){
        String query = "SELECT COUNT(*) FROM notification_record nr JOIN notification n ON n.id = nr.notification_id WHERE building_id = :buildingId";

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("buildingId", buildingId);

        return namedParameterJdbcTemplate.queryForObject(query, params, Long.class);
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

    public void update(NotificationRecord notificationRecord){
        String query = "UPDATE notification_record SET track_complete=:trackComplete WHERE notification_id=:notificationId AND member_id=:memberId";
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("notificationId", notificationRecord.getNotificationId());
        params.addValue("memberId", notificationRecord.getMemberId());
        params.addValue("trackComplete", notificationRecord.isTrackComplete());

        namedParameterJdbcTemplate.update(query, params);
    }


}
