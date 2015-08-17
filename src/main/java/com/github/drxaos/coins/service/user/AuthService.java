package com.github.drxaos.coins.service.user;

import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.application.factory.Component;
import com.github.drxaos.coins.domain.User;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Component
public class AuthService {

    @Inject
    Db db;

    public User checkAuth(String name, String password) throws TypedSqlException {
        try {
            Dao<User, Long> users = db.getDao(User.class);
            List<User> userList = users.queryForFieldValues(Collections.singletonMap("name", name));
            if (!userList.isEmpty() && userList.get(0).checkPassword(password)) {
                return userList.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new TypedSqlException(e, TypedSqlException.Type.UNKNOWN);
        }
    }

}
