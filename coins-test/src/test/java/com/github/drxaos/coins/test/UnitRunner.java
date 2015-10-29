package com.github.drxaos.coins.test;

import com.github.drxaos.coins.StartupFixes;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class UnitRunner extends BlockJUnit4ClassRunner {
    static {
        // init
        new StartupFixes();
    }

    public UnitRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
    }

    @Override
    protected Object createTest() throws Exception {
        return super.createTest();
    }
}
