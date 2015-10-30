package com.github.drxaos.coins.test;

import com.github.drxaos.coins.StartupFixes;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class AbstractRunner extends BlockJUnit4ClassRunner {
    static {
        // init
        new StartupFixes();
    }

    public AbstractRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }
}
