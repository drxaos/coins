package com.github.drxaos.coins.application.database;

import com.github.drxaos.coins.application.factory.Component;

import java.sql.SQLException;

@Component
public class DbDialect {

    public TypedSqlException.Type resolveErrorType(SQLException e) {
        return TypedSqlException.Type.UNKNOWN;
    }

}
