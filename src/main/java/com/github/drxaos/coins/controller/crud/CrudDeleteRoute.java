package com.github.drxaos.coins.controller.crud;

import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.database.Entity;
import com.github.drxaos.coins.application.database.OptimisticLockException;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.controller.SecureRoute;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Collections;

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
        T entity = dao.queryForId(id);
        if (entity != null) {
            try {
                try {
                    process(entity);

                    boolean done = false;
                    for (int i = 0; i < 3; i++) {
                        try {
                            entity.delete();
                            done = true;
                            break;
                        } catch (OptimisticLockException e) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e1) {
                                // ok
                            }
                        }
                    }
                    if (!done) {
                        throw new TypedSqlException(null, TypedSqlException.Type.LOCK);
                    }
                } catch (TypedSqlException e) {
                    response().status(400);
                    return new CrudError("cannot-delete", sqlExceptionData(entity, e));
                }
            } catch (CrudException e) {
                response().status(e.httpCode);
                return new CrudError(e.getMessage(), e.data);
            }

            return new CrudSuccess("entity-deleted", Collections.singletonMap("id", id));
        } else {
            response().status(404);
            return new CrudError("not-found", Collections.singletonMap("id", id));
        }
    }

    abstract protected void process(T entity) throws CrudException;

    abstract protected Object sqlExceptionData(T entity, TypedSqlException e) throws CrudException;
}
