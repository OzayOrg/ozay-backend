package com.ozay.backend.resultsetextractor;

import com.ozay.backend.model.TempUser;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naofumiezaki on 11/17/15.
 */
public class TempUserSetExtractor implements ResultSetExtractor {
    @Override
    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<TempUser> tempUsers = new ArrayList<TempUser>();
        while(resultSet.next()){
            TempUser tempUser = new TempUser();
            tempUser.setId(resultSet.getLong("id"));
            tempUser.setFirstName(resultSet.getString("first_name"));
            tempUser.setLastName(resultSet.getString("last_name"));
            tempUser.setEmail(resultSet.getString("email"));
            tempUser.setActivationKey(resultSet.getString("activation_key"));
            tempUser.setActivated(resultSet.getBoolean("activated"));
            tempUsers.add(tempUser);
        }
        return tempUsers;
    }
}
