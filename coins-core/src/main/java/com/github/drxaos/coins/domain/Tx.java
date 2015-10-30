package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.database.Entity;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.application.validation.ValidationResult;
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

    @Override
    protected void validator(ValidationResult result) {
        if (incomeAccount != null && date.before(incomeAccount.created())) {
            result.fieldError("date", "date.before.incomeAccount", date, incomeAccount.created());
        }
        if (incomeAccount != null && incomeAccount.isClosed() && date.after(incomeAccount.closed())) {
            result.fieldError("date", "date.after.incomeAccount", date, incomeAccount.closed());
        }
        if (outcomeAccount != null && date.before(outcomeAccount.created())) {
            result.fieldError("date", "date.before.outcomeAccount", date, outcomeAccount.created());
        }
        if (outcomeAccount != null && outcomeAccount.isClosed() && date.after(outcomeAccount.closed())) {
            result.fieldError("date", "date.after.outcomeAccount", date, outcomeAccount.closed());
        }
    }
}
