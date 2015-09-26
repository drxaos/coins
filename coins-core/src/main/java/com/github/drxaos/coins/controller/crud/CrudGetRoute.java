package com.github.drxaos.coins.controller.crud;

import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.database.Entity;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.controller.RestHandler;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Collections;

public abstract class CrudGetRoute<T extends Entity> extends RestHandler<T, Object> {
    @Inject
    protected Db db;

    protected Class<T> collectionType;

    public CrudGetRoute(Class<T> collectionType) {
        this.collectionType = collectionType;
    }

    @Override
    public Object handle() throws SQLException, TypedSqlException {
        Dao<T, Long> dao = db.getDao(collectionType);
        Long id = Long.parseLong(transport.params(":id"));
        T entity = dao.queryForId(id);
        if (entity != null) {
            try {
                entity = process(entity);
            } catch (CrudException e) {
                transport.status(e.httpCode);
                return new CrudError(e.getMessage(), e.data);
            }
            return entity;
        } else {
            transport.status(404);
            return new CrudError("not-found", Collections.singletonMap("id", id));
        }
    }

    abstract protected T process(T entity) throws CrudException;
}
