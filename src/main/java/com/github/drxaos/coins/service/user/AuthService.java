package com.github.drxaos.coins.service.user;

import com.github.drxaos.coins.application.Autowire;
import com.github.drxaos.coins.application.Component;
import com.github.drxaos.coins.application.Db;
import com.github.drxaos.coins.domain.User;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Component
public class AuthService {

    @Autowire
    Db db;

    public boolean checkAuth(String name, String password) throws SQLException {
        Dao<User, Long> users = db.getDao(User.class);
        List<User> userList = users.queryForFieldValues(Collections.singletonMap("name", name));
        if (!userList.isEmpty() && userList.get(0).checkPassword(password)) {
            return true;
        }
        return false;
    }

}
