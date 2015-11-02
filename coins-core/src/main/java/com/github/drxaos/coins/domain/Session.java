package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.database.Entity;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.google.common.collect.Maps;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true, chain = true)
@EqualsAndHashCode(callSuper = false)
@DatabaseTable(tableName = "sessions")
public class Session extends Entity<Session> {

    @DatabaseField(canBeNull = false, uniqueIndex = true)
    String name;

    @DatabaseField(canBeNull = false)
    Date created;

    @DatabaseField(canBeNull = false)
    Date accessed;

    @DatabaseField(canBeNull = true)
    Long userId;

    @DatabaseField(canBeNull = true)
    String label;

    @DatabaseField(canBeNull = false, dataType = DataType.BYTE_ARRAY)
    byte[] data;

    public Session() throws TypedSqlException {
    }

    public Session dataMap(Map<String, ?> data) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(b);
        HashMap<String, Object> toSave = new HashMap<>(Maps.filterKeys(data, (k) -> k.startsWith("__")));
        stream.writeObject(toSave);
        stream.close();
        this.data = b.toByteArray();

        return this;
    }

    public Map<String, Object> dataMap() throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(data);
        ObjectInputStream stream = new ObjectInputStream(b);
        HashMap<String, Object> map = (HashMap<String, Object>) stream.readObject();
        stream.close();

        return map;
    }
}
