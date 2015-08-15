package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.database.Entity;
import com.j256.ormlite.field.DataType;
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
@DatabaseTable(tableName = "accounts")
public class Account extends Entity<Account> {

    public enum Type {
        CASH, // 0
        BANK  // 1
    }

    @DatabaseField(canBeNull = false, uniqueCombo = true, foreign = true)
    User user;
    @DatabaseField(canBeNull = false, uniqueCombo = true)
    String name;
    @DatabaseField(dataType = DataType.ENUM_INTEGER, unknownEnumName = "CASH")
    Type type;
    @DatabaseField(canBeNull = false)
    String currency;
    @DatabaseField(canBeNull = false)
    BigDecimal value;
    @DatabaseField(canBeNull = false)
    Date created;
    @DatabaseField(canBeNull = true)
    Date closed;

    public Account() throws SQLException {
    }
}
