package com.github.drxaos.coins.controller.auth;

import com.google.gson.annotations.Expose;

public class AccessDeniedResponse {

    @Expose
    String error = "unauthorized";

    @Expose
    boolean noauth = true;

}
