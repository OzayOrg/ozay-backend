package com.ozay.backend.web.rest.dto.pages;

import com.ozay.backend.model.Notification;
import com.ozay.backend.model.NotificationRecord;

import java.util.List;

/**
 * Created by naofumiezaki on 11/25/15.
 */
public class PageNotificationRecordDTO {
    private Long totalNumOfPages;
    private List<Notification> notifications;

    public Long getTotalNumOfPages() {
        return totalNumOfPages;
    }

    public void setTotalNumOfPages(Long totalNumOfPages) {
        this.totalNumOfPages = totalNumOfPages;
    }

    public List<Notification> getNotificationRecords() {
        return notifications;
    }

    public void setNotificationRecords(List<Notification> notificationRecords) {
        this.notifications = notificationRecords;
    }


}
