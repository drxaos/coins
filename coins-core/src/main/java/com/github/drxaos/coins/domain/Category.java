package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.database.Entity;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.validation.ValidationResult;
import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true, chain = true)
@EqualsAndHashCode(callSuper = false)
@DatabaseTable(tableName = "categories")
public class Category extends Entity<Category> {

    @DatabaseField(canBeNull = false, uniqueCombo = true, foreign = true)
    User user;

    @Expose
    @DatabaseField(canBeNull = false, uniqueCombo = true)
    String name;

    @Expose
    @DatabaseField(canBeNull = false)
    boolean expense = false;

    @Expose
    @DatabaseField(canBeNull = false)
    boolean income = false;

    public Category() throws TypedSqlException {
    }

    @Override
    protected void validator(ValidationResult result) {
        if (name == null || name.isEmpty()) {
            result.fieldError("name", "name-is-empty");
        }
        if (user == null) {
            result.fieldError("user", "user-is-empty");
        }
    }
}
