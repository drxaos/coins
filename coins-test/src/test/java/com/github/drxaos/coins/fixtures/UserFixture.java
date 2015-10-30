package com.github.drxaos.coins.fixtures;

import com.github.drxaos.coins.application.database.OptimisticLockException;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.validation.ValidationException;
import com.github.drxaos.coins.domain.User;

public class UserFixture extends Fixtures {

    public User user;

    public UserFixture(String name) throws TypedSqlException, ValidationException, OptimisticLockException {
        user = new User()
                .name(name)
                .email(name + "@example.com")
                .lang("RU")
                .password("password")
                .save();
    }

    public UserFixture() throws OptimisticLockException, ValidationException, TypedSqlException {
        this("user");
    }
}
