package com.github.drxaos.coins.controller.auth;

import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.application.test.Fixtures;
import com.github.drxaos.coins.fixtures.SessionsFixture;
import com.github.drxaos.coins.fixtures.UsersFixture;
import com.github.drxaos.coins.mock.DateUtilMock;
import com.github.drxaos.coins.test.RestTestCase;
import org.junit.Test;

public class AuthControllerRestTest extends RestTestCase {

    @Inject
    Fixtures fixtures;

    @Inject
    DateUtilMock dateUtilMock;

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
        SessionsFixture sessionsFixture = fixtures.require(SessionsFixture.class);

        setSessionId(sessionsFixture.user2Session1.name());

        Response response = get("/api/v1/auth/whoami");
        assertEquals("user2", response.query("username"));
        assertEquals("ru", response.query("lang"));

        response = post("/api/v1/auth/sign_out", null);
        assertEquals("ok", response.query("$"));

        response = get("/api/v1/auth/whoami");
        assertEquals("", response.query("username"));
        assertEquals("en", response.query("lang"));
    }

    @Test
    public void test_changePassword() throws Exception {
        dateUtilMock.setMockDate(dateUtilMock.parseDateTime("01.01.2000", "12:00:00"));

        SessionsFixture sessionsFixture = fixtures.require(SessionsFixture.class);

        setCurrentSession("session1");
        setSessionId(sessionsFixture.user2Session1.name());
        setCurrentSession("session2");
        setSessionId(sessionsFixture.user2Session2.name());

        // check sessions

        setCurrentSession("session1");
        Response response = get("/api/v1/auth/whoami");
        assertEquals("user2", response.query("username"));
        assertEquals("ru", response.query("lang"));

        setCurrentSession("session2");
        response = get("/api/v1/auth/whoami");
        assertEquals("user2", response.query("username"));
        assertEquals("ru", response.query("lang"));

        // change password with wrong oldPassword

        setCurrentSession("session1");
        response = post("/api/v1/auth/password",
                "{oldPassword: 'wrong', newPassword: 'qwerty'}");
        assertEquals("field-error", response.query("error"));
        assertEquals("oldPassword", response.query("data.errors[0].fieldName"));

        dateUtilMock.setMockDate(dateUtilMock.parseDateTime("01.01.2000", "12:01:00"));

        setCurrentSession("session1");
        response = get("/api/v1/auth/whoami");
        assertEquals("user2", response.query("username"));
        assertEquals("ru", response.query("lang"));

        setCurrentSession("session2");
        response = get("/api/v1/auth/whoami");
        assertEquals("user2", response.query("username"));
        assertEquals("ru", response.query("lang"));

        // change password with correct oldPassword

        setCurrentSession("session1");
        response = post("/api/v1/auth/password",
                "{oldPassword: 'password2', newPassword: 'qwerty'}");
        assertTrue(response.query("success"));

        dateUtilMock.setMockDate(dateUtilMock.parseDateTime("01.01.2000", "12:02:00"));

        setCurrentSession("session1");
        response = get("/api/v1/auth/whoami");
        assertEquals("user2", response.query("username"));
        assertEquals("ru", response.query("lang"));

        setCurrentSession("session2");
        response = get("/api/v1/auth/whoami");
        assertEquals("", response.query("username"));
        assertEquals("en", response.query("lang"));
    }
}
