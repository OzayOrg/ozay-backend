package com.ozay.rowmapper;

import com.ozay.model.Notification;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sarvesh on 31-10-2015.
 */

public class EmailCountMapper extends NotificationMapper {
    public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
        Notification notification = new Notification();
        notification.setId(rs.getLong("id"));
        notification.setBuildingId(rs.getInt("building_id"));
        notification.setNotice(rs.getString("notice"));
        notification.setIssueDate(new DateTime(rs.getDate("issue_date")));
        notification.setCreatedBy(rs.getString("created_by"));
        notification.setCreatedDate(new DateTime(rs.getDate("created_date")));
        notification.setSubject(rs.getString("subject"));

        notification.setEmailCount(rs.getInt("emailCount"));

        return notification;
    }
}
