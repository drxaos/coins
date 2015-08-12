package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.database.Entity;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true, chain = true)
@EqualsAndHashCode(callSuper = false)
@DatabaseTable(tableName = "transactions")
public class Tx extends Entity<Tx> {

    @DatabaseField(canBeNull = false, foreign = true)
    User user;
    @DatabaseField(canBeNull = false)
    Date date;
    @DatabaseField(canBeNull = true, foreign = true)
    Category category;

    @DatabaseField(canBeNull = true, foreign = true)
    Account outcomeAccount;
    @DatabaseField(canBeNull = true)
    BigDecimal outcomeValue;

    @DatabaseField(canBeNull = true, foreign = true)
    Account incomeAccount;
    @DatabaseField(canBeNull = true)
    BigDecimal incomeValue;

    @DatabaseField(canBeNull = true)
    String comment;

    public Tx() throws SQLException {
    }
}
