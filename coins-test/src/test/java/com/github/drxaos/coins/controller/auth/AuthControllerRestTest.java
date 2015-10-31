package com.github.drxaos.coins.controller.auth;

import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.application.test.Fixtures;
import com.github.drxaos.coins.fixtures.UsersFixture;
import com.github.drxaos.coins.test.RestTestCase;
import org.junit.Test;

public class AuthControllerRestTest extends RestTestCase {

    @Inject
    Fixtures fixtures;

    @Test
    public void test_whoAmI() throws Exception {
        Response response = get("/api/v1/auth/whoami");
        assertEquals("", response.query("username"));
        assertEquals("en", response.query("lang"));
    }

    @Test
    public void test_signIn_noUser() throws Exception {
        Response response = post("/api/v1/auth/sign_in",
                "{user: 'test', password: 'test'}");
        assertFalse(response.query("success"));

        response = get("/api/v1/auth/whoami");
        assertEquals("", response.query("username"));
        assertEquals("en", response.query("lang"));
    }

    @Test
    public void test_signIn() throws Exception {
        fixtures.require(UsersFixture.class);

        Response response = post("/api/v1/auth/sign_in",
                "{user: 'test', password: 'test'}");
        assertFalse(response.query("success"));

        response = get("/api/v1/auth/whoami");
        assertEquals("", response.query("username"));
        assertEquals("en", response.query("lang"));

        response = post("/api/v1/auth/sign_in",
                "{user: 'user1', password: 'password1'}");
        assertTrue(response.query("success"));

        response = get("/api/v1/auth/whoami");
        assertEquals("user1", response.query("username"));
        assertEquals("ru", response.query("lang"));
    }

    @Test
    public void test_signOut() throws Exception {
        fixtures.require(UsersFixture.class);

        Response response = post("/api/v1/auth/sign_in",
                "{user: 'user2', password: 'password2'}");
        assertTrue(response.query("success"));

        response = get("/api/v1/auth/whoami");
        assertEquals("user2", response.query("username"));
        assertEquals("ru", response.query("lang"));

        response = post("/api/v1/auth/sign_out", null);
        assertEquals("ok", response.query("$"));

        response = get("/api/v1/auth/whoami");
        assertEquals("", response.query("username"));
        assertEquals("en", response.query("lang"));
    }
}
