package com.github.drxaos.coins.application.database;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.config.ApplicationProps;
import com.github.drxaos.coins.application.events.ApplicationInit;
import com.github.drxaos.coins.application.events.ApplicationStop;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.application.factory.AutowiringFactory;
import com.github.drxaos.coins.application.factory.Component;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Component
public class Db implements ApplicationInit, ApplicationStop {

    @Autowire
    ApplicationProps props;

    private AutowiringFactory factory;

    private JdbcConnectionSource connectionSource;
    private Map<Class, Dao> daoMap = new HashMap<>();

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {
        factory = application.getFactory();
        Entity.factory = factory;

        String jdbcUrl = props.getString("jdbc.url", "jdbc:h2:mem:test");

        try {
            connectionSource = new JdbcConnectionSource(jdbcUrl);
        } catch (SQLException e) {
            throw new ApplicationInitializationException("Cannot connect to database", e);
        }

        List<Class> domainClasses = application.getFactory().getClassesByAnnotation(DatabaseTable.class);
        for (Class domainClass : domainClasses) {
            try {
                if (!getDao(domainClass).isTableExists()) {
                    TableUtils.createTable(getConnectionSource(), domainClass);
                }

                // Check schema
                Dao dao = getDao(domainClass);
                PreparedQuery preparedQuery = dao.queryBuilder().orderBy("id", true).limit(1l).prepare();
                List<Object> list = dao.query(preparedQuery);
                if (list == null || list.size() > 1) {
                    throw new ApplicationInitializationException("Database error");
                }

            } catch (SQLException | TypedSqlException e) {
                throw new ApplicationInitializationException("Cannot create table for " + domainClass, e);
            }
        }
    }

    ThreadLocal<Boolean> inTransaction = new ThreadLocal<>();

    public <T> T callInTransaction(final Callable<T> callable) throws SQLException {
        Boolean alreadiInTransaction = inTransaction.get();
        if (alreadiInTransaction != null && alreadiInTransaction) {
            try {
                return callable.call();
            } catch (SQLException | RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("tx error", e);
            }
        } else {
            inTransaction.set(true);
            T res = TransactionManager.callInTransaction(getConnectionSource(), callable);
            inTransaction.set(false);
            return res;
        }
    }

    public JdbcConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public <T> Dao<T, Long> getDao(Class<T> domainCls) throws TypedSqlException {
        if (!daoMap.containsKey(domainCls)) {
            try {
                daoMap.put(domainCls, DaoManager.createDao(connectionSource, domainCls));
            } catch (SQLException e) {
                throw new TypedSqlException(e, TypedSqlException.Type.ORM);
            }
        }
        return daoMap.get(domainCls);
    }

    @Override
    public void onApplicationStop(Application application) throws ApplicationInitializationException {
        try {
            connectionSource.close();
        } catch (SQLException e) {
            throw new ApplicationInitializationException("Cannot close connection", e);
        }
    }
}
