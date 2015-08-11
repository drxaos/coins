package com.github.drxaos.coins.controller.auth;

public class AuthResponse {

    Boolean success = false;
    String error = "";

    public AuthResponse(Boolean success, String error) {
        this.success = success;
        this.error = error;
    }
}
