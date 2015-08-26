package com.github.drxaos.coins.spark.sessions;

import com.github.drxaos.coins.application.database.Entity;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true, chain = true)
@EqualsAndHashCode(callSuper = false)
@DatabaseTable(tableName = "sessions")
public class StoredSession extends Entity<StoredSession> {

    @DatabaseField(canBeNull = false, uniqueIndex = true)
    String name;

    @DatabaseField(canBeNull = false)
    Date created;

    @DatabaseField(canBeNull = false)
    Date accessed;

    @DatabaseField(canBeNull = false, dataType = DataType.BYTE_ARRAY)
    byte[] data;

    public StoredSession() throws TypedSqlException {
    }
}
