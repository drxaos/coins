package com.github.drxaos.coins.application.database.h2;

import com.github.drxaos.coins.application.database.DbDialect;
import com.github.drxaos.coins.application.database.TypedSqlException;
import org.h2.jdbc.JdbcSQLException;

import java.sql.SQLException;

public class H2Dialect extends DbDialect {

    @Override
    public TypedSqlException.Type resolveErrorType(SQLException e) {
        int errorCode = e.getErrorCode();
        if (errorCode == 0 && e.getCause() instanceof JdbcSQLException) {
            errorCode = ((JdbcSQLException) e.getCause()).getErrorCode();
        }
        switch (errorCode) {
            case 50200:
                return TypedSqlException.Type.LOCK;
            case 23505:
                return TypedSqlException.Type.CONFLICT;
            default:
                return TypedSqlException.Type.UNKNOWN;
        }
    }
}
