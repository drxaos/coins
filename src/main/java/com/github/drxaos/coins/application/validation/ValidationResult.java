package com.github.drxaos.coins.application.validation;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult<T> {

    @Expose
    private T object;

    @Expose
    private List<ValidationError> errors = new ArrayList<>();

    public ValidationResult(T object) {
    }

    public T getObject() {
        return object;
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void fieldError(String fieldName) {
        errors.add(new ValidationError().fieldName(fieldName));
    }

    public void fieldError(String fieldName, String message) {
        errors.add(new ValidationError().fieldName(fieldName).message(message));
    }

    public void fieldError(String fieldName, String message, Object... args) {
        errors.add(new ValidationError().fieldName(fieldName).message(message).args(args));
    }

    public void objectError() {
        errors.add(new ValidationError().fieldName(""));
    }

    public void objectError(String message) {
        errors.add(new ValidationError().fieldName("").message(message));
    }

    public void objectError(String message, Object... args) {
        errors.add(new ValidationError().fieldName("").message(message).args(args));
    }
}