package com.github.drxaos.coins.utils;

import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.test.IntegrationTestCase;
import org.junit.Test;

import java.util.Date;

public class DateUtilIntegrationTest extends IntegrationTestCase {

    @Inject
    DateUtil dateUtil;

    @Test
    public void test_now() throws Exception {
        Date now = dateUtil.now();
        assertNotNull(now);
    }

    @Test
    public void test_clearTime() throws Exception {
        Date date = new Date(1000000000l);
        Date clearedDate = dateUtil.clearTime(date);
        assertEquals(950400000l, clearedDate.getTime());
    }

    @Test
    public void test_clearTime_midnight() throws Exception {
        Date date = new Date(0);
        Date clearedDate = dateUtil.clearTime(date);
        assertEquals(0, clearedDate.getTime());
    }

    @Test
    public void test_format() throws Exception {
        Date date = new Date(0);
        String str = dateUtil.format(date);
        assertEquals("01 января 1970 г.", str);

        date = new Date(100000000000l);
        str = dateUtil.format(date);
        assertEquals("03 марта 1973 г.", str);
    }

    @Test
    public void test_formatDateTime() throws Exception {
        Date date = new Date(0);
        String str = dateUtil.formatDateTime(date);
        assertEquals("00:00:00, 01 января 1970 г.", str);

        date = new Date(100000000000l);
        str = dateUtil.formatDateTime(date);
        assertEquals("09:46:40, 03 марта 1973 г.", str);
    }
}
