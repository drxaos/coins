package com.github.drxaos.coins.application;

public class ApplicationInitializationException extends Exception {
    public ApplicationInitializationException() {
    }

    public ApplicationInitializationException(String message) {
        super(message);
    }

    public ApplicationInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationInitializationException(Throwable cause) {
        super(cause);
    }
}
