package com.github.drxaos.coins;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.TimeZone;

public class StartupFixes {
    static {
        // force default encoding
        try {
            String property = System.getProperty("file.encoding");
            if (property != null && property.toUpperCase().equals("UTF-8")) {
                System.setProperty("file.encoding", "UTF-8");
                Field charset = Charset.class.getDeclaredField("defaultCharset");
                charset.setAccessible(true);
                charset.set(null, null);
            }
        } catch (Exception e) {
            //skip
        }

        // force UTC timezone
        try {
            System.setProperty("user.timezone", "UTC");
            TimeZone.setDefault(null);
        } catch (Exception e) {
            //skip
        }
    }
}
