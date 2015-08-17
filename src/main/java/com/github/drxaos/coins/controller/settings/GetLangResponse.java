package com.github.drxaos.coins.controller.settings;


import com.google.gson.annotations.Expose;

public class GetLangResponse {
    @Expose
    String lang;

    public GetLangResponse(String lang) {
        this.lang = lang;
    }
}
