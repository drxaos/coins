package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.Entity;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.SQLException;

@Data
@Accessors(fluent = true, chain = true)
@EqualsAndHashCode(callSuper = false)
@DatabaseTable(tableName = "categories")
public class Category extends Entity<Category> {
    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false, uniqueCombo = true, foreign = true)
    User user;

    @DatabaseField(canBeNull = false, uniqueCombo = true)
    String name;

    @DatabaseField(canBeNull = false)
    boolean expense = false;

    @DatabaseField(canBeNull = false)
    boolean income = false;

    public Category() throws SQLException {
    }
}
