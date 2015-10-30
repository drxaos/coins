package com.github.drxaos.coins.test;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

/**
 * Used from maven surefire plugin
 */
public class TestListener extends RunListener {

    public static long getPID() {
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(processName.split("@")[0]);
    }

    @Override
    public void testRunStarted(Description description) throws Exception {
        H2DbHelper.runName = "testdb" + getPID();
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        H2DbHelper.clear(false);
    }
}
