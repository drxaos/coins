package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.database.Entity;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.application.validation.ValidationResult;
import com.github.drxaos.coins.utils.DateUtil;
import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DataType;
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
@DatabaseTable(tableName = "accounts")
public class Account extends Entity<Account> {

    public enum Type {
        CASH, // 0
        BANK  // 1
    }

    @DatabaseField(canBeNull = false, foreign = true, index = true)
    User user;

    @Expose
    @DatabaseField(canBeNull = false, index = true)
    String name;

    @Expose
    @DatabaseField(canBeNull = false, dataType = DataType.ENUM_INTEGER, unknownEnumName = "CASH")
    Type type = Type.CASH;

    @Expose
    @DatabaseField(canBeNull = false)
    String currency = "RUR";

    @Expose
    @DatabaseField(canBeNull = false, columnDefinition = "DECIMAL(20,2)")
    BigDecimal startValue = BigDecimal.ZERO;

    @Expose
    BigDecimal value;

    @DatabaseField(canBeNull = false, index = true)
    Date created;

    @DatabaseField(canBeNull = true, index = true)
    Date closed;

    public Account() throws TypedSqlException {
    }

    @Override
    protected void validator(ValidationResult result) {
        if (name == null || name.isEmpty()) {
            result.fieldError("name", "name-is-empty");
        }
        if (user == null) {
            result.fieldError("user", "user-is-empty");
        }
        if (type == null) {
            result.fieldError("type", "type-is-empty");
        }
        if (startValue == null) {
            result.fieldError("startValue", "startValue-is-empty");
        }
        if (created == null) {
            result.fieldError("created", "created-is-empty");
        }
    }
}
