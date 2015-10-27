package com.github.drxaos.coins.controller.auth;


import com.github.drxaos.coins.application.controller.Command;
import com.google.gson.annotations.Expose;

public class ChangePasswordRequest extends Command<ChangePasswordRequest> {
    @Expose
    String oldPassword;

    @Expose
    String newPassword;
}
