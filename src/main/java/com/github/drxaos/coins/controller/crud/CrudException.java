package com.github.drxaos.coins.controller.crud;

public class CrudException extends Exception {
    Integer httpCode;

    public CrudException(String message, Integer httpCode) {
        super(message);
        this.httpCode = httpCode;
    }
}
