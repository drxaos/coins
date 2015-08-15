package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.database.Entity;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.application.validation.ValidationResult;
import com.github.drxaos.coins.service.user.PasswordService;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.sql.SQLException;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true, chain = true)
@EqualsAndHashCode(callSuper = false)
@DatabaseTable(tableName = "users")
public class User extends Entity<User> {

    @Autowire
    transient PasswordService passwordService;

    @DatabaseField(canBeNull = false, uniqueIndex = true)
    String name;

    @DatabaseField(canBeNull = false)
    String password;

    @DatabaseField(canBeNull = false)
    String email;

    @ForeignCollectionField(eager = false)
    ForeignCollection<Category> categories;

    @ForeignCollectionField(eager = false)
    ForeignCollection<Account> accounts;

    public User() throws SQLException {
    }

    @Override
    protected void validator(ValidationResult result) {
        if (name == null || name.isEmpty()) {
            result.fieldError("name", "name-is-empty");
        } else if (name.length() < 3) {
            result.fieldError("name", "name-min-size", 3);
        } else if (name.length() > 100) {
            result.fieldError("name", "name-max-size", 100);
        }
        if (email == null || email.isEmpty()) {
            result.fieldError("email", "email-is-empty");
        } else if (!email.contains("@")) {
            result.fieldError("email", "email-wrong");
        }
    }

    public User password(String password) {
        this.password = passwordService.encode(password);
        return this;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(passwordService.encode(password));
    }
}
