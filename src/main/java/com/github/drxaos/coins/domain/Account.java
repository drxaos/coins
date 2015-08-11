package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.Entity;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.SQLException;

@Data
@Accessors(fluent = true, chain = true)
@EqualsAndHashCode(callSuper = false)
@DatabaseTable(tableName = "accounts")
public class Account extends Entity<Account> {
    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false, uniqueCombo = true, foreign = true)
    User user;
    @DatabaseField(canBeNull = false, uniqueCombo = true)
    String name;
    @DatabaseField(canBeNull = false)
    String currency;
    @DatabaseField(canBeNull = false)
    BigDecimal value;

    public Account() throws SQLException {
    }
}
