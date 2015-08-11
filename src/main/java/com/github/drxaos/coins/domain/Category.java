package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.Entity;
import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
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
@DatabaseTable(tableName = "categories")
public class Category extends Entity<Category> {
    @Expose
    @DatabaseField(generatedId = true)
    Long id;

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

    public Category() throws SQLException {
    }
}
