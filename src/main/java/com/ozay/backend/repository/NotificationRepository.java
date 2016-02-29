package com.ozay.backend.repository;

import com.ozay.backend.model.Notification;
import com.ozay.backend.resultsetextractor.NotificationSetExtractor;
import com.ozay.backend.security.SecurityUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by naofumiezaki on 10/31/15.
 */

@Repository
public class NotificationRepository {
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Notification> findAllByBuildingId(Long buildingId, Long offset){
        int limit = 20;
        offset = offset * limit;
        String query = "SELECT * FROM notification WHERE building_id = :buildingId order by created_date desc LIMIT :limit OFFSET :offset";

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("buildingId", buildingId);
        params.addValue("limit", limit);
        params.addValue("offset", offset);

        return (List<Notification>)namedParameterJdbcTemplate.query(query, params, new NotificationSetExtractor());
    }

    public List<Notification> searchNotifications(Long buildingId, String[] items){

        String query = "SELECT * FROM notification where building_id = :buildingId ";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", buildingId);

        String queryForList = "";
        for(int i = 0; i< items.length;i++){
            String param = "param" + i;
            params.addValue(param, items[i]);
            if(i != 0){
                queryForList += " OR ";
            }
            queryForList += " LOWER(notice) LIKE :" + param +
                " OR LOWER(subject) LIKE :" + param;
        }
        if(items.length > 0){
            query += " AND (";
            query += queryForList;
            query += ")";
        }
        return (List<Notification>)namedParameterJdbcTemplate.query(query, params, new NotificationSetExtractor());
    };

    public Long countAllByBuildingId(Long buildingId){
        String query = "SELECT COUNT(*) FROM notification WHERE building_id = :buildingId";

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("buildingId", buildingId);

        return namedParameterJdbcTemplate.queryForObject(query, params, Long.class);
    }

    public List<Notification> searchNotificationWithLimit(Long buildingId, Long limit){

        String query = "SELECT * from notification WHERE id in(" +
            "SELECT MAX(id) " +
            "from notification " +
            "WHERE building_id=:buildingId " +
            "GROUP BY subject " +
            ") " +
            "ORDER BY created_date DESC " +
            "LIMIT :limit ";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", buildingId);
        params.addValue("limit", limit);

        return (List<Notification>)namedParameterJdbcTemplate.query(query, params, new NotificationSetExtractor());
    };

    public Notification findOne(Long notificationId){

        String query = "SELECT * FROM notification WHERE id = :notificationId";

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("notificationId", notificationId);
        List<Notification> notifications = (List<Notification>)namedParameterJdbcTemplate.query(query, params, new NotificationSetExtractor());
        if(notifications.size() == 1){
            return notifications.get(0);
        } else {
            return null;
        }
    }


    public void create(Notification notification){
        String query = "INSERT INTO notification (building_id, notice, issue_date, created_by, created_date, subject, track) VALUES (:buildingId, :notice, :issueDate, :createdBy, NOW(), :subject, :track) RETURNING id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", notification.getBuildingId());
        params.addValue("notice", notification.getNotice());
        params.addValue("issueDate", new Timestamp(notification.getIssueDate().getMillis()));
        params.addValue("createdBy", SecurityUtils.getCurrentLogin());
        params.addValue("subject", notification.getSubject());
        params.addValue("track", notification.isTrack());
        Long id = namedParameterJdbcTemplate.queryForObject(query, params, Long.class);
        notification.setId(id);
    }

    public void update(Notification notification){
        String query = "UPDATE notification SET email_count=:emailCount WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", notification.getId());
        params.addValue("emailCount", notification.getEmailCount());

        namedParameterJdbcTemplate.update(query, params);

    }
}
