package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.Autowire;
import com.github.drxaos.coins.application.Entity;
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

    @DatabaseField(generatedId = true)
    Long id;

    @DatabaseField(canBeNull = false, uniqueIndex = true)
    String name;

    @DatabaseField(canBeNull = false)
    String password;

    @ForeignCollectionField(eager = false)
    ForeignCollection<Category> categories;

    @ForeignCollectionField(eager = false)
    ForeignCollection<Account> accounts;

    public User() throws SQLException {
    }

    public User password(String password) {
        this.password = passwordService.encode(password);
        return this;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(passwordService.encode(password));
    }
}
