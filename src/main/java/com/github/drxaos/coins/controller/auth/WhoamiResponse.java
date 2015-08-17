package com.github.drxaos.coins.controller.auth;

import com.google.gson.annotations.Expose;

public class WhoamiResponse {

    @Expose
    String username;

    @Expose
    String lang;

    public WhoamiResponse(String username, String lang) {
        this.username = username;
        this.lang = lang;
    }
}
