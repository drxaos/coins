package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.database.Entity;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.utils.DateUtil;
import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true, chain = true)
@EqualsAndHashCode(callSuper = false)
@DatabaseTable(tableName = "transactions")
public class Tx extends Entity<Tx> {

    @Inject
    transient DateUtil dateUtil;

    @DatabaseField(canBeNull = false, foreign = true)
    User user;

    @Expose
    @DatabaseField(canBeNull = false, index = true)
    Date date;

    @Expose
    @DatabaseField(canBeNull = true, foreign = true, index = true)
    Category category;

    @Expose
    @DatabaseField(canBeNull = true, foreign = true, index = true)
    Account outcomeAccount;

    @Expose
    @DatabaseField(canBeNull = true, columnDefinition = "DECIMAL(20,2)")
    BigDecimal outcomeValue;

    @Expose
    @DatabaseField(canBeNull = true, foreign = true, index = true)
    Account incomeAccount;

    @Expose
    @DatabaseField(canBeNull = true, columnDefinition = "DECIMAL(20,2)")
    BigDecimal incomeValue;

    @Expose
    @DatabaseField(canBeNull = true)
    String comment;

    public String name() {
        String catStr = category == null ? "???" : category.name();
        String dateStr = date == null ? "???" : dateUtil.formatDateShort(date);
        return catStr + " (" + dateStr + ")";
    }

    public Tx() throws TypedSqlException {
    }
}
