package com.github.drxaos.coins.service.user;

import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.test.IntegrationTestCase;
import org.junit.Test;

public class PasswordServiceIntegrationTest extends IntegrationTestCase {

    @Inject
    PasswordService passwordService;

    @Test
    public void test_encode() throws Exception {

        String qwerty = passwordService.encode("qwerty");
        assertEquals("FevVmJXavBKqlUrdR5EzfprVisWZ/b3Oh+8rGhvn5XQ=", qwerty);

    }
}
