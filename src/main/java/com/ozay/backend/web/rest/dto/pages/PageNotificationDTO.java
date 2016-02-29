package com.ozay.backend.web.rest.dto.pages;

import com.ozay.backend.model.Member;
import com.ozay.backend.model.Notification;
import com.ozay.backend.model.Role;

import java.util.List;

/**
 * Created by naofumiezaki on 11/23/15.
 */
public class PageNotificationDTO {
    private List<Notification> notifications;
    private List<Role> roles;
    private List<Member> members;

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
