package com.github.drxaos.coins.controller.crud;

import com.google.gson.annotations.Expose;

public class CrudSuccess {
    @Expose
    String success;

    public CrudSuccess(String success) {
        this.success = success;
    }
}
