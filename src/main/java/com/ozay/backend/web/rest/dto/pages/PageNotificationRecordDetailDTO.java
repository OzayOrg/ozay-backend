package com.ozay.backend.web.rest.dto.pages;

import com.ozay.backend.model.Notification;
import com.ozay.backend.model.NotificationRecord;

import java.util.List;

/**
 * Created by naofumiezaki on 11/25/15.
 */
public class PageNotificationRecordDetailDTO {
    private Notification notification;
    private List<NotificationRecord> notificationRecords;

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public List<NotificationRecord> getNotificationRecords() {
        return notificationRecords;
    }

    public void setNotificationRecords(List<NotificationRecord> notificationRecords) {
        this.notificationRecords = notificationRecords;
    }
}
