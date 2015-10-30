package com.github.drxaos.coins.test;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

abstract public class AbstractTestCase extends TestCase {

    @FunctionalInterface
    public interface Code {
        void call() throws Throwable;
    }

    public static <E extends Throwable> E shouldFail(Class<E> type, Code code) {
        try {
            code.call();
        } catch (Throwable throwable) {
            assertEquals(type, throwable.getClass());
            return (E) throwable;
        }
        fail("code should throw " + type.getSimpleName());
        throw new AssertionFailedError();
    }
}
