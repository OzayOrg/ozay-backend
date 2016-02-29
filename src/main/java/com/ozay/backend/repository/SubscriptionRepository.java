package com.ozay.backend.repository;

import com.ozay.backend.model.Subscription;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * Created by naofumiezaki on 11/1/15.
 */
@Repository
public class SubscriptionRepository {
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void create(Subscription subscription){
        String query = "INSERT INTO subscription (user_id, created_date, date_from, date_to) " +
            "VALUES(:userId, now(), now(), now() + interval '1 year')";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", subscription.getUserId());

        namedParameterJdbcTemplate.update(query,params);
    }
}
