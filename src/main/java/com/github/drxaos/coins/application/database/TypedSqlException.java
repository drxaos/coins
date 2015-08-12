package com.github.drxaos.coins.application.database;

public class TypedSqlException extends Exception {
    public enum Type {
        UNKNOWN, CONFLICT, LOCK, ORM
    }

    private Type type;

    public TypedSqlException(Throwable cause, Type type) {
        super("database-error", cause);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
