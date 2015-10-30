package com.github.drxaos.coins.service.user;

import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.application.test.Fixtures;
import com.github.drxaos.coins.domain.User;
import com.github.drxaos.coins.errors.CheckPasswordException;
import com.github.drxaos.coins.fixtures.UsersFixture;
import com.github.drxaos.coins.test.IntegrationTestCase;
import org.junit.Test;

public class AuthServiceIntegrationTest extends IntegrationTestCase {

    @Inject
    AuthService authService;

    @Inject
    Fixtures fixtures;

    @Test
    public void test_checkAuth_noUsers() throws Exception {
        User user = authService.checkAuth("test", "test");
        assertNull(user);
    }

    @Test
    public void test_checkAuth_success() throws Exception {
        UsersFixture usersFixture = fixtures.require(UsersFixture.class);

        User user = authService.checkAuth("user1", "password1");
        assertNotNull(user);
        assertEquals("user1", user.name());

        user = authService.checkAuth("user2", "password2");
        assertNotNull(user);
        assertEquals("user2", user.name());
    }

    @Test
    public void test_checkAuth_error() throws Exception {
        UsersFixture usersFixture = fixtures.require(UsersFixture.class);

        User user = authService.checkAuth("user1", "password2");
        assertNull(user);

        user = authService.checkAuth("user2", "password1");
        assertNull(user);

        user = authService.checkAuth("user3", "password");
        assertNull(user);
    }

    @Test
    public void test_changePassword() throws Exception {
        UsersFixture usersFixture = fixtures.require(UsersFixture.class);

        authService.changePassword(usersFixture.user1, "password1", "qwerty");
        usersFixture.user1.refresh();
        assertEquals("FevVmJXavBKqlUrdR5EzfprVisWZ/b3Oh+8rGhvn5XQ=", usersFixture.user1.password());

        authService.changePassword(usersFixture.user1, "qwerty", "!@#$%^&*()");
        usersFixture.user1.refresh();
        assertEquals("FeFqc9OvRipI1DJTErsOEFcpfeKJGHFLbQ4fxXhvwJI=", usersFixture.user1.password());
    }

    @Test
    public void test_changePassword_error() throws Exception {
        UsersFixture usersFixture = fixtures.require(UsersFixture.class);

        shouldFail(CheckPasswordException.class, () -> authService.changePassword(usersFixture.user1, "qwerty", "asdf"));
        authService.changePassword(usersFixture.user1, "password1", "qwerty");
        authService.changePassword(usersFixture.user1, "qwerty", "asdf");
        shouldFail(CheckPasswordException.class, () -> authService.changePassword(usersFixture.user1, "password1", "qwerty"));

    }
}
