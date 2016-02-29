package com.ozay.backend.model;

/**
 * Created by naofumiezaki on 11/29/15.
 */
public class AccountPermission {
    private Long subscriberId;
    private String key;

    public Long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "AccountPermission{" +
            "subscriberId='" + subscriberId + '\'' +
            "key='" + key + '\'' +

            "}";
    }
}
