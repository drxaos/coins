package com.github.drxaos.coins.controller.crud;

import com.google.gson.annotations.Expose;

public class CrudSuccess {
    @Expose
    String success;

    @Expose
    Object data;

    public CrudSuccess(String success, Object data) {
        this.success = success;
        this.data = data;
    }
}
