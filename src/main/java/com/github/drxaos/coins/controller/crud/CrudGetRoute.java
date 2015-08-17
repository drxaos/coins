package com.github.drxaos.coins.controller.crud;

import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.database.Entity;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.controller.SecureRoute;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public abstract class CrudGetRoute<T extends Entity> extends SecureRoute<T, Object> {
    @Inject
    Db db;

    protected Class<T> collectionType;

    public CrudGetRoute(Class<T> collectionType) {
        this.collectionType = collectionType;
    }

    @Override
    public Object handle() throws SQLException, TypedSqlException {
        Dao<T, Long> dao = db.getDao(collectionType);
        T entity = dao.queryForId(Long.parseLong(request().params(":id")));
        try {
            entity = process(entity);
        } catch (CrudException e) {
            response().status(e.httpCode);
            return e.getMessage();
        }
        if (entity != null) {
            return entity;
        } else {
            response().status(404);
            return "Not found";
        }
    }

    abstract protected T process(T entity) throws CrudException;
}
