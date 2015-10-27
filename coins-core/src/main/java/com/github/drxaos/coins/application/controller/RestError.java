package com.github.drxaos.coins.application.controller;

import com.google.gson.annotations.Expose;

public class RestError {
    @Expose
    String error;

    @Expose
    Object data;

    public RestError(String error, Object data) {
        this.error = error;
        this.data = data;
    }
}
