package com.github.drxaos.coins.controller.crud;

import com.github.drxaos.coins.application.database.Entity;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.validation.ValidationException;
import com.github.drxaos.coins.controller.SecureRoute;

public abstract class CrudCreateRoute<T extends Entity> extends SecureRoute<T, Object> {
    protected Class<T> collectionType;

    public CrudCreateRoute(Class<T> collectionType) {
        this.collectionType = collectionType;
    }

    @Override
    public Object handle() {
        T entity;
        try {
            entity = input(collectionType);
            entity = process(entity);
            entity.id(null);
            entity.save();
        } catch (ValidationException e) {
            response().status(400);
            return e.getValidationResult();
        } catch (TypedSqlException e) {
            if (e.getType() == TypedSqlException.Type.CONFLICT) {
                response().status(409);
                return new CrudError("duplicate-entity");
            } else {
                response().status(400);
                return new CrudError("invalid-entity");
            }
        } catch (CrudException e) {
            response().status(e.httpCode);
            return new CrudError(e.getMessage());
        }

        response().status(201);
        return entity;
    }

    abstract protected T process(T entity) throws CrudException;
}
