package com.github.drxaos.coins.service.settings;

import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.application.factory.Component;
import com.github.drxaos.coins.application.validation.ValidationException;
import com.github.drxaos.coins.domain.User;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

@Component
public class SettingsService {

    @Inject
    Db db;

    public User changeLang(User user, String lang) throws ValidationException, TypedSqlException {
        try {
            Dao<User, Long> users = db.getDao(User.class);
            User userForChange = users.queryForId(user.id());
            userForChange
                    .lang(lang)
                    .save();

            return userForChange;
        } catch (SQLException e) {
            throw new TypedSqlException(e, TypedSqlException.Type.UNKNOWN);
        }
    }

}
