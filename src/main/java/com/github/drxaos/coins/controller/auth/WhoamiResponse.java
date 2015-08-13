package com.github.drxaos.coins.controller.auth;

import com.google.gson.annotations.Expose;

public class WhoamiResponse {

    @Expose
    String username;

    public WhoamiResponse(String username) {
        this.username = username;
    }
}
