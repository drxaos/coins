package com.github.drxaos.coins.controller.crud;

import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.database.Entity;
import com.github.drxaos.coins.application.database.OptimisticLockException;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.application.validation.ValidationException;
import com.github.drxaos.coins.controller.RestHandler;

public abstract class CrudCreateRoute<T extends Entity> extends RestHandler<T, Object> {

    @Inject
    protected Db db;

    protected Class<T> collectionType;

    public CrudCreateRoute(Class<T> collectionType) {
        this.collectionType = collectionType;
    }

    @Override
    public Object handle() {
        T entity = null;
        try {
            try {
                entity = transport.input(collectionType);
                entity = process(entity);
                entity.id(null);
                try {
                    entity.save();
                } catch (OptimisticLockException e) {
                    // should not happen
                    throw new TypedSqlException(null, TypedSqlException.Type.LOCK);
                }
            } catch (ValidationException e) {
                transport.status(400);
                return new CrudError("error-fields", e.getValidationResult());
            } catch (TypedSqlException e) {
                if (e.getType() == TypedSqlException.Type.CONFLICT) {
                    transport.status(409);
                    return new CrudError("duplicate-entity", sqlExceptionData(entity, e));
                } else {
                    transport.status(400);
                    return new CrudError("cannot-create", sqlExceptionData(entity, e));
                }
            }
        } catch (CrudException e) {
            transport.status(e.httpCode);
            return new CrudError(e.getMessage(), e.data);
        }

        transport.status(201);
        return entity;
    }

    abstract protected T process(T entity) throws CrudException;

    abstract protected Object sqlExceptionData(T entity, TypedSqlException e) throws CrudException;
}
