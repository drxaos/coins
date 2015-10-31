package com.github.drxaos.coins.fixtures;

import com.github.drxaos.coins.application.test.Fixture;
import com.github.drxaos.coins.domain.User;

public class UsersFixture extends Fixture {

    public User user1;
    public User user2;

    @Override
    protected void init() throws Exception {
        user1 = new User()
                .name("user1")
                .email("user1@example.com")
                .lang("ru")
                .password("password1")
                .save();
        put("user1", user1);

        user2 = new User()
                .name("user2")
                .email("user2@example.com")
                .lang("ru")
                .password("password2")
                .save();
        put("user2", user1);
    }
}
