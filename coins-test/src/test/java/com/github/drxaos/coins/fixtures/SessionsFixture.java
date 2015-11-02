package com.github.drxaos.coins.fixtures;

import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.application.test.Fixture;
import com.github.drxaos.coins.domain.Session;
import com.github.drxaos.coins.utils.DateUtil;
import com.google.common.collect.ImmutableMap;

public class SessionsFixture extends Fixture {

    public Session user1Session1;
    public Session user2Session1;
    public Session user2Session2;

    @Inject
    DateUtil dateUtil;

    @Override
    protected void init() throws Exception {
        UsersFixture usersFixture = fixtures.require(UsersFixture.class);

        user1Session1 = new Session()
                .name("user1Session1JSESSIONID")
                .created(dateUtil.now())
                .accessed(dateUtil.now())
                .userId(usersFixture.user1.id())
                .dataMap(ImmutableMap.<String, Object>builder()
                        .put("__userId", usersFixture.user1.id())
                        .build())
                .save();
        put("user1Session1", user1Session1);

        user2Session1 = new Session()
                .name("user2Session1JSESSIONID")
                .created(dateUtil.now())
                .accessed(dateUtil.now())
                .userId(usersFixture.user2.id())
                .dataMap(ImmutableMap.<String, Object>builder()
                        .put("__userId", usersFixture.user2.id())
                        .build())
                .save();
        put("user2Session1", user2Session1);

        user2Session2 = new Session()
                .name("user2Session2JSESSIONID")
                .created(dateUtil.now())
                .accessed(dateUtil.now())
                .userId(usersFixture.user2.id())
                .dataMap(ImmutableMap.<String, Object>builder()
                        .put("__userId", usersFixture.user2.id())
                        .build())
                .save();
        put("user2Session2", user2Session2);
    }
}
