package com.github.drxaos.coins.application.validation;

import com.google.gson.annotations.Expose;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true, chain = true)
@EqualsAndHashCode(callSuper = false)
public class ValidationError {

    @Expose
    public String fieldName;

    @Expose
    public String message;

    @Expose
    public Object[] args;

    public ValidationError() {
    }
}
