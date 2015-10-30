package com.github.drxaos.coins.fixtures;

public class FixturesLoadingException extends RuntimeException {
    public FixturesLoadingException() {
    }

    public FixturesLoadingException(String message) {
        super(message);
    }

    public FixturesLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public FixturesLoadingException(Throwable cause) {
        super(cause);
    }
}
