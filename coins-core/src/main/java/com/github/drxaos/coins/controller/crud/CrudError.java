package com.github.drxaos.coins.controller.crud;

import com.google.gson.annotations.Expose;

public class CrudError {
    @Expose
    String error;

    @Expose
    Object data;

    public CrudError(String error, Object data) {
        this.error = error;
        this.data = data;
    }
}
