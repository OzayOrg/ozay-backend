package com.ozay.rowmapper;

/**
 * Created by RGVK on 29-10-2015.
 */
import com.ozay.model.Notification;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmailCountRowMapper extends NotificationMapper {

    public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
        Notification notification = new Notification();
        notification.setEmailCount(rs.getInt("emailCount"));//to get the email count to be displayed as a part of notifications attribute

        return notification;
    }
}
