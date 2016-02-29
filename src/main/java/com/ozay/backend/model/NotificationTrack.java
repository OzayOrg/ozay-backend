package com.ozay.backend.model;

import java.util.List;

import org.joda.time.DateTime;

/**
 * Created by naofumiezaki on 10/31/15.
 */
public class NotificationTrack {
    public Long memberId;
    public Long notificationId;

    private boolean success;
    private String email;
    private String note;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    private String subject;

    private Member member;

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    private DateTime createdDate;




    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

  //  public List<NotificationTrack> getNotificationTrackList() {
    //    return notificationTrackList;
    //}

 //   public void setNotificationRecordList(List<NotificationRecord> notificationRecordList) {
   //     this.notificationRecordList = notificationRecordList;
   // }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificationTrack notificationTrack = (NotificationTrack) o;

        if (notificationId != null ? !notificationId.equals(notificationTrack.notificationId) : notificationTrack.notificationId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (notificationId ^ (notificationId >>> 32));
    }

    @Override
    public String toString() {
        return "NotificationTrack{" +
            "id=" + notificationId +
            ", createdDate='" + createdDate + "'" +
            ", member='" + member + "'" +


            '}';
    }


}
