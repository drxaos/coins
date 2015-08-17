package com.github.drxaos.coins.controller.crud;

import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.database.Entity;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.controller.SecureRoute;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public abstract class CrudDeleteRoute<T extends Entity> extends SecureRoute<T, Object> {
    @Inject
    Db db;

    protected Class<T> collectionType;

    public CrudDeleteRoute(Class<T> collectionType) {
        this.collectionType = collectionType;
    }

    @Override
    public Object handle() throws SQLException, TypedSqlException {
        Dao<T, Long> dao = db.getDao(collectionType);
        Long id = Long.parseLong(request().params(":id"));
        T entity = dao.queryForId(Long.parseLong(request().params(":id")));
        if (entity != null) {
            try {
                process(entity);
                entity.delete();
            } catch (TypedSqlException e) {
                if (e.getType() == TypedSqlException.Type.CONFLICT) {
                    response().status(409);
                    return "duplicate-entity";
                } else {
                    response().status(400);
                    return "invalid-entity";
                }
            } catch (CrudException e) {
                response().status(e.httpCode);
                return e.getMessage();
            }

            return "Entity with id " + id + " deleted";
        } else {
            response().status(404); // 404 Not found
            return "Not found";
        }
    }

    abstract protected void process(T entity) throws CrudException;
}
