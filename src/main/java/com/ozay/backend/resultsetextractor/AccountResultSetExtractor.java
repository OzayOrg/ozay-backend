package com.ozay.backend.resultsetextractor;

import com.ozay.backend.model.AccountInformation;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by naofumiezaki on 10/31/15.
 */
public class AccountResultSetExtractor implements ResultSetExtractor {

    public Object extractData(ResultSet rs) throws SQLException {
        List<AccountInformation> list = new ArrayList<AccountInformation>();
        Map<String, String> map = new HashMap<String, String>();

        AccountInformation accountInformation = null;
        Set<String> authorities = new HashSet<String>();
        while(rs.next()){
            if(accountInformation == null){
                accountInformation = new AccountInformation();
                accountInformation.setSubscriberId(rs.getLong("s_user_id"));
                accountInformation.setOrganizationId(rs.getLong("organization_id"));
                accountInformation.setSubscriptionId(rs.getLong("s_id"));
            }
            if(rs.getString("rp_name") != null){
                map.put(rs.getString("rp_name"), rs.getString("rp_name"));
            }

            if(rs.getString("op_name") != null){
                map.put(rs.getString("op_name"), rs.getString("op_name"));
            }


        }

        if(accountInformation != null){

            if(map.size() > 0){

                accountInformation.setAuthorities( new ArrayList<String>(map.values()));
            }
            list.add(accountInformation);
        }

        return list;
    }
}
