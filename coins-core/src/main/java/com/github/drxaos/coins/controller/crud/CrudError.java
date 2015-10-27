package com.github.drxaos.coins.controller.crud;

import com.github.drxaos.coins.application.controller.RestError;

public class CrudError extends RestError {

    public CrudError(String error, Object data) {
        super(error, data);
    }
}
