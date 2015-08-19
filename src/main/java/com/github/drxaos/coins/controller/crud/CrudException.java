package com.github.drxaos.coins.controller.crud;

public class CrudException extends Exception {
    Integer httpCode;
    Object data;

    public CrudException(Integer httpCode, String message, Object data) {
        super(message);
        this.httpCode = httpCode;
        this.data = data;
    }
}
