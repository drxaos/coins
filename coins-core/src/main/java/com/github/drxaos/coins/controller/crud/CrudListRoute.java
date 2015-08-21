package com.github.drxaos.coins.controller.crud;

import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.database.Entity;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.controller.RestHandler;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public abstract class CrudListRoute<T extends Entity> extends RestHandler<T, Object> {
    @Inject
    Db db;

    protected Class<T> collectionType;

    public CrudListRoute(Class<T> collectionType) {
        this.collectionType = collectionType;
    }

    @Override
    public Object handle() throws SQLException, TypedSqlException {
        Dao<T, Long> dao = db.getDao(collectionType);
        QueryBuilder<T, Long> queryBuilder = dao.queryBuilder();
        try {
            processQuery(queryBuilder);
        } catch (CrudException e) {
            transport.status(e.httpCode);
            return new CrudError(e.getMessage(), e.data);
        }

        List<T> list = queryBuilder.query();
        try {
            list = processList(list);
        } catch (CrudException e) {
            transport.status(e.httpCode);
            return new CrudError(e.getMessage(), e.data);
        }
        return list;
    }

    abstract protected void processQuery(QueryBuilder<T, Long> queryBuilder) throws CrudException;

    abstract protected List<T> processList(List<T> list) throws CrudException;
}
