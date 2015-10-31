package com.github.drxaos.coins.fixtures;

import com.github.drxaos.coins.application.test.Fixture;
import com.github.drxaos.coins.domain.User;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true, chain = true)
public class UserFixture extends Fixture {

    public User user;

    public UserFixture() {
    }

    String fixtureName = null;

    public UserFixture(String fixtureName) {
        this.fixtureName = fixtureName;
    }

    @Setter
    String name = "user";
    @Setter
    String email = null;
    @Setter
    String lang = "ru";
    @Setter
    String password = "password";

    @Override
    protected void init() throws Exception {
        if (email == null) {
            email = name + "@example.com";
        }

        user = new User()
                .name(name)
                .email(email)
                .lang(lang)
                .password(password)
                .save();
        if (fixtureName != null) {
            put("customUser" + fixtureName, user);
        }
    }
}
