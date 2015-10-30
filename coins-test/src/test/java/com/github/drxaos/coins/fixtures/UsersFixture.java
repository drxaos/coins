package com.github.drxaos.coins.fixtures;

import com.github.drxaos.coins.application.database.OptimisticLockException;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.validation.ValidationException;
import com.github.drxaos.coins.domain.User;

public class UsersFixture extends Fixtures {

    public User user1;
    public User user2;

    public UsersFixture() throws TypedSqlException, ValidationException, OptimisticLockException {
        user1 = new User()
                .name("user1")
                .email("user1@example.com")
                .lang("RU")
                .password("password1")
                .save();

        user2 = new User()
                .name("user2")
                .email("user2@example.com")
                .lang("RU")
                .password("password2")
                .save();
    }
}
