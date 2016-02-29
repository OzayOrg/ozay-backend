package com.ozay.backend.model;

import org.joda.time.DateTime;

/**
 * Created by naofumiezaki on 10/31/15.
 */
public class InvitedMember {
    private Long id;
    private Long memberId;
    private String langKey;
    private String activationKey;
    private boolean activated;
    private String createdBy;
    private DateTime createdDate;
    private String updatedBy;
    private DateTime updatedDate;
    private DateTime activatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public DateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(DateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public DateTime getActivatedDate() {
        return activatedDate;
    }

    public void setActivatedDate(DateTime activatedDate) {
        this.activatedDate = activatedDate;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public String toString() {
        return "InvitedUser{" +
            "id='" + id + '\'' +
            "memberId='" + memberId + '\'' +
            "activationKey='" + activationKey + '\'' +
            "activated='" + activated + '\'' +
            "}";
    }

}
