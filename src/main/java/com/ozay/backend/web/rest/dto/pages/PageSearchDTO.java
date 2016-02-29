package com.ozay.backend.web.rest.dto.pages;

import com.ozay.backend.model.Member;
import com.ozay.backend.model.Notification;

import java.util.List;

/**
 * Created by naofumiezaki on 11/30/15.
 */
public class PageSearchDTO {
    private List<Notification> notifications;
    private List<Member> members;

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
