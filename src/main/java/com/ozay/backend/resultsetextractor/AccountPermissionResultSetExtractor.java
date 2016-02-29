package com.ozay.backend.resultsetextractor;

import com.ozay.backend.model.AccountPermission;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naofumiezaki on 10/31/15.
 */
public class AccountPermissionResultSetExtractor implements ResultSetExtractor {

    public Object extractData(ResultSet resultSet) throws SQLException {
        List<AccountPermission> list = new ArrayList<AccountPermission>();

        while(resultSet.next()){
            AccountPermission accountInformation = new AccountPermission();
            accountInformation.setSubscriberId(resultSet.getLong("subscription_id"));
            accountInformation.setKey(resultSet.getString("key"));
            list.add(accountInformation);
        }
        return list;
    }
}
