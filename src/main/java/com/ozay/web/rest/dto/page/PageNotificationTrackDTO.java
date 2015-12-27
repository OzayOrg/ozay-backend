package com.ozay.web.rest.dto.page;

import com.ozay.model.NotificationRecord;

import java.util.List;

/**
 * Created by naofumiezaki on 12/20/15.
 */
public class PageNotificationTrackDTO {
    private List<NotificationRecord> notificationRecords;

    public List<NotificationRecord> getNotificationRecords() {
        return notificationRecords;
    }

    public void setNotificationRecords(List<NotificationRecord> notificationRecords) {
        this.notificationRecords = notificationRecords;
    }
}
