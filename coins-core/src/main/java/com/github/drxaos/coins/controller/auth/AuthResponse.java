package com.github.drxaos.coins.controller.auth;

import com.google.gson.annotations.Expose;

public class AuthResponse {

    @Expose
    Boolean success = false;

    @Expose
    String error = "";

    public AuthResponse(Boolean success, String error) {
        this.success = success;
        this.error = error;
    }
}
