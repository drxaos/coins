package com.github.drxaos.coins.controller.crud;

import com.google.gson.annotations.Expose;

public class CrudError {
    @Expose
    String error;

    public CrudError(String error) {
        this.error = error;
    }
}
