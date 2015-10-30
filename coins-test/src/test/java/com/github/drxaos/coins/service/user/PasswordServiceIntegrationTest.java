package com.github.drxaos.coins.service.user;

import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.test.IntegrationTestCase;
import org.junit.Test;

public class PasswordServiceIntegrationTest extends IntegrationTestCase {

    @Inject
    PasswordService passwordService;

    @Test
    public void test_encode() throws Exception {
        String hash = passwordService.encode("qwerty");
        assertEquals("FevVmJXavBKqlUrdR5EzfprVisWZ/b3Oh+8rGhvn5XQ=", hash);

        hash = passwordService.encode("");
        assertEquals("S3aGuxLADeVk6RVtNJVlozdEmcv8fPgXM4WtfuU0UE4=", hash);

        hash = passwordService.encode("!@#$%^&*()");
        assertEquals("FeFqc9OvRipI1DJTErsOEFcpfeKJGHFLbQ4fxXhvwJI=", hash);
    }

    @Test
    public void test_check() throws Exception {
        String hash = passwordService.encode("qwerty");

        assertTrue(passwordService.check("qwerty", hash));
        assertFalse(passwordService.check("Qwerty", hash));
        assertFalse(passwordService.check(" qwerty", hash));
        assertFalse(passwordService.check("qwerty ", hash));
        assertFalse(passwordService.check("qwert", hash));
        assertFalse(passwordService.check("", hash));
    }
}
