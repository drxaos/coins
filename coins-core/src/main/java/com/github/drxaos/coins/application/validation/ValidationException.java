package com.github.drxaos.coins.application.validation;

public class ValidationException extends Exception {
    private ValidationResult validationResult;

    public ValidationException(ValidationResult result) {
        this.validationResult = result;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }
}
