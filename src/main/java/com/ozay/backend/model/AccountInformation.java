package com.ozay.backend.model;

import java.util.List;

/**
 * Created by naofumiezaki on 10/31/15.
 */
public class AccountInformation {
    private Long subscriberId;
    private Long subscriptionId;
    private Long organizationSubscriberId;
    private Long organizationId;
    private List<String> authorities;

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getOrganizationSubscriberId() {
        return organizationSubscriberId;
    }

    public void setOrganizationSubscriberId(Long organizationSubscriberId) {
        this.organizationSubscriberId = organizationSubscriberId;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "AccountInformation{" +
            "subscriberId='" + subscriberId + '\'' +
            "subscriptionId='" + subscriptionId + '\'' +
            "organizationId='" + organizationId + '\'' +
            "Authority='" + authorities + '\'' +
            "}";
    }
}
