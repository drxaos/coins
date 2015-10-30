package com.github.drxaos.coins.controller.auth;

import com.github.drxaos.coins.test.FunctionTestCase;
import org.junit.Test;

public class AuthControllerFunctionTest extends FunctionTestCase {

    @Test
    public void test_whoami() throws Exception {
        Response response = get("/api/v1/auth/whoami", null);
        assertEquals("", response.query("username"));
        assertEquals("en", response.query("lang"));
    }
}
