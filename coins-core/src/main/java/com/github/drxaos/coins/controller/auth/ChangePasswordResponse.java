package com.github.drxaos.coins.controller.auth;


import com.google.gson.annotations.Expose;

public class ChangePasswordResponse {
    @Expose
    Boolean success;

    public ChangePasswordResponse(Boolean success) {
        this.success = success;
    }
}
