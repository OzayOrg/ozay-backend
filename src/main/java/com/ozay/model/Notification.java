package com.ozay.model;

import org.joda.time.DateTime;

import java.util.List;




public class Notification{

    private Long id;

    private Integer buildingId;

    private String notice;

    private String subject;

    private DateTime issueDate;


    private String createdBy;

    private DateTime createdDate;

    public Integer emailCount;


    private List<NotificationRecord> notificationRecordList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public int getEmailCount(int emailCount) { return emailCount; }

    public void setEmailCount(int emailCount) { this.emailCount = emailCount; }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public DateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(DateTime issueDate) {
        this.issueDate = issueDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public List<NotificationRecord> getNotificationRecordList() {
        return notificationRecordList;
    }

    public void setNotificationRecordList(List<NotificationRecord> notificationRecordList) {
        this.notificationRecordList = notificationRecordList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Notification notification = (Notification) o;

        if (id != null ? !id.equals(notification.id) : notification.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", buildingId='" + buildingId + "'" +
                ", notice='" + notice + "'" +
                ", issueDate='" + issueDate + "'" +
                ", createdBy='" + createdBy + "'" +
                ", createdDate='" + createdDate + "'" +
                ", emailCount='" + emailCount + "'" +
                '}';
    }
}
