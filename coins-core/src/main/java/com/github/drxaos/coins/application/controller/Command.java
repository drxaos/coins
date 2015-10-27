package com.github.drxaos.coins.application.controller;

import com.github.drxaos.coins.application.validation.ValidationError;
import com.github.drxaos.coins.application.validation.ValidationResult;

import java.util.Collections;
import java.util.List;

public class Command<T> {

    protected ValidationResult<T> validationResult;

    public ValidationResult<T> validate() {
        ValidationResult result = new ValidationResult<T>((T) this);
        validator(result);
        return result;
    }

    public boolean hasErrors() {
        return validationResult != null && validationResult.hasErrors();
    }

    public List<ValidationError> getErrors() {
        return validationResult != null ? validationResult.getErrors() : Collections.<ValidationError>emptyList();
    }

    protected void validator(ValidationResult result) {
    }

}
