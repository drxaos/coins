package com.github.drxaos.coins.test;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class IntegrationRunner extends BlockJUnit4ClassRunner {
    public IntegrationRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
    }

}
