package com.github.drxaos.coins.application;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.BaseDaoEnabled;

import java.sql.SQLException;

public class Entity extends BaseDaoEnabled {

    static AutowiringFactory factory;
    Db db;

    public Entity() throws SQLException {
        if (factory != null) {
            factory.autowire(this);
            db = factory.findObject(Db.class);
            setDao(db.getDao(this.getClass()));
        }
    }

    private void checkForDao() throws SQLException {
        if (dao == null) {
            throw new SQLException("Dao has not been set on " + getClass() + " object: " + this);
        }
    }

    public Dao.CreateOrUpdateStatus createOrUpdate() throws SQLException {
        checkForDao();
        return dao.createOrUpdate(this);
    }

}
