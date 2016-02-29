package com.ozay.backend.model;

import org.joda.time.DateTime;

/**
 * Created by naofumiezaki on 10/31/15.
 */
public class Subscription {
    private Long id;
    private Long userId;
    private DateTime createdDate;
    private DateTime dateFrom;
    private DateTime dateTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public DateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(DateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public DateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(DateTime dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return "Subscription{" +
            "id='" + id + '\'' +
            "userId='" + userId + '\'' +
            "dateFrom='" + dateFrom + '\'' +
            "dateTO='" + dateTo + '\'' +
            "}";
    }
}
