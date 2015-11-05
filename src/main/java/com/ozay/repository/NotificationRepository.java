package com.ozay.repository;

import com.ozay.model.Notification;
import com.ozay.rowmapper.EmailCountMapper;
import com.ozay.rowmapper.NotificationMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.List;

/**
 * Spring Data JPA repository for the Notification entity.
 */
@Repository
public class NotificationRepository{

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Notification> findAllByBuilding(Long buildingId){
        String query = "select S.*, D.emailCount from notification S, (select notification_id, count(*) as email_count from notification_record group by notification_id) D where S.id = D.notification_id";// this code was changed.
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", buildingId);

       // return namedParameterJdbcTemplate.query(query, params, new NotificationMapper());

        return namedParameterJdbcTemplate.query(query, params, new EmailCountMapper());

    };

    public Notification findOne(Long id){
        String query = "SELECT * FROM notification where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return (Notification)namedParameterJdbcTemplate.queryForObject(query, params, new NotificationMapper());
    }

    public List<Notification> searchNotificationWithLimit(Long buildingId, Long limit){

        String query = "SELECT * from notification WHERE id in(" +
            "SELECT MAX(id) " +
            "from notification " +
            "WHERE building_id=:buildingId " +
            "GROUP BY subject " +
            ") " +
            "ORDER BY created_date DESC " +
            "LIMIT :limit " ;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", buildingId);
        params.addValue("limit", limit);

        return namedParameterJdbcTemplate.query(query, params, new NotificationMapper());
    };


    public List<Notification> searchNotification(Long buildingId, String[] items){
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
        return namedParameterJdbcTemplate.query(query, params, new NotificationMapper());
    };

    public Long create(Notification notification){
        String query = "INSERT INTO notification (building_id, notice, issue_date, created_by, created_date, subject)" +
            " VALUES (:buildingId, :notice, :issueDate, :createdBy, NOW(), :subject) RETURNING id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", notification.getBuildingId());
        params.addValue("notice", notification.getNotice());
        params.addValue("issueDate", new Timestamp(notification.getIssueDate().getMillis()));
        params.addValue("createdBy", notification.getCreatedBy());
        params.addValue("subject", notification.getSubject());

        return namedParameterJdbcTemplate.queryForObject(query, params, Long.class);
    }
}

