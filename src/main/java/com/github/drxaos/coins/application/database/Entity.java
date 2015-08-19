package com.github.drxaos.coins.application.database;

import com.github.drxaos.coins.application.factory.AutowiringFactory;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.application.validation.ValidationError;
import com.github.drxaos.coins.application.validation.ValidationException;
import com.github.drxaos.coins.application.validation.ValidationResult;
import com.google.gson.annotations.Expose;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class Entity<T> implements Serializable {

    @Expose
    @DatabaseField(generatedId = true)
    Long id;

    @DatabaseField(version = true)
    private Long version;

    public Long id() {
        return id;
    }

    public T id(Long id) {
        this.id = id;
        return (T) this;
    }

    public Long version() {
        return version;
    }

    public T version(Long version) {
        this.version = version;
        return (T) this;
    }

    static AutowiringFactory factory;

    @Inject
    Db db;

    @Inject
    DbDialect dialect;

    protected transient Dao dao;

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public Entity() throws TypedSqlException {
        if (factory != null) {
            factory.autowire(this);
            setDao(db.getDao(this.getClass()));
        }
    }

    protected ValidationResult<T> validationResult;

    public ValidationResult<T> validate() {
        ValidationResult result = new ValidationResult<T>((T) this);
        validator(result);
        return result;
    }

    public boolean hasErrors() {
        return validationResult != null && validationResult.hasErrors();
    }

    public List<ValidationError> getErrors() {
        return validationResult != null ? validationResult.getErrors() : Collections.emptyList();
    }

    protected void validator(ValidationResult result) {
    }

    protected void checkForDao() throws TypedSqlException {
        if (dao == null) {
            throw new TypedSqlException(new SQLException("Dao has not been set on " + getClass() + " object: " + this), TypedSqlException.Type.ORM);
        }
    }

    public T save() throws ValidationException, TypedSqlException, OptimisticLockException {
        validate();
        if (hasErrors()) {
            throw new ValidationException(validationResult);
        }
        checkForDao();
        try {
            Dao.CreateOrUpdateStatus status = dao.createOrUpdate((T) this);
            if (status.getNumLinesChanged() != 1) {
                throw new OptimisticLockException("Entity not deleted");
            }
            return (T) this;
        } catch (SQLException e) {
            throw new TypedSqlException(e, dialect.resolveErrorType(e));
        }
    }

    public void delete() throws TypedSqlException, OptimisticLockException {
        checkForDao();
        T t = (T) this;
        try {
            int rows = dao.delete(t);
            if (rows != 1) {
                throw new OptimisticLockException("Entity not deleted");
            }
        } catch (SQLException e) {
            throw new TypedSqlException(e, dialect.resolveErrorType(e));
        }
    }

    public int refresh() throws TypedSqlException {
        checkForDao();
        T t = (T) this;
        try {
            return dao.refresh(t);
        } catch (SQLException e) {
            throw new TypedSqlException(e, dialect.resolveErrorType(e));
        }
    }
}
