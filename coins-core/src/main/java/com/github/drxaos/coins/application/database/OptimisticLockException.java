package com.github.drxaos.coins.application.database;

public class OptimisticLockException extends Exception {
    public OptimisticLockException(String message) {
        super(message);
    }
}
