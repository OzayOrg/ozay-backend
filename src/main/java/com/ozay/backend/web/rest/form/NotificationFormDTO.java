package com.ozay.backend.web.rest.form;

import com.ozay.backend.model.Member;
import com.ozay.backend.model.Notification;
import com.ozay.backend.model.Role;

import java.util.List;

/**
 * Created by naofumiezaki on 11/23/15.
 */
public class NotificationFormDTO {
    private Notification notification;
    private List<Member> members;
    private List<NotificationRoleFormDTO> roles;
    private boolean result;

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public List<NotificationRoleFormDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<NotificationRoleFormDTO> roles) {
        this.roles = roles;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NotificationFormDTO{" +
            "notification='" + notification + '\'' +
            "List<Member>='" + members + '\'' +
            "roles='" + roles + '\'' +
            "}";
    }
 }
