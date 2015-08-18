package com.github.drxaos.coins.application.database;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.config.ApplicationProps;
import com.github.drxaos.coins.application.events.ApplicationInit;
import com.github.drxaos.coins.application.events.ApplicationStop;
import com.github.drxaos.coins.application.factory.AutowiringFactory;
import com.github.drxaos.coins.application.factory.Component;
import com.github.drxaos.coins.application.factory.Inject;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableInfo;
import com.j256.ormlite.table.TableUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Slf4j
@Component
public class Db implements ApplicationInit, ApplicationStop {

    @Inject
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
        Map<Dao, List<String>> foreignKeyCreate = new HashMap<>();
        for (Class domainClass : domainClasses) {
            try {
                Dao dao = getDao(domainClass);
                if (!dao.isTableExists()) {
                    TableUtils.createTable(getConnectionSource(), domainClass);
                    TableInfo tableInfo = ((BaseDaoImpl) dao).getTableInfo();
                    for (FieldType fieldType : tableInfo.getFieldTypes()) {
                        if (fieldType.isForeign()) {
                            FieldType foreignIdField = fieldType.getForeignIdField();
                            String thisTable = fieldType.getTableName();
                            String thisColumn = fieldType.getColumnName();
                            String foreignTable = foreignIdField.getTableName();
                            String foreignColumn = foreignIdField.getColumnName();

                            String sql = "ALTER TABLE " + thisTable +
                                    " ADD CONSTRAINT FK_" + thisTable + "_" + thisColumn + "_" + foreignTable + "_" + foreignColumn +
                                    " FOREIGN KEY (" + thisColumn + ")" +
                                    " REFERENCES " + foreignTable + "(" + foreignColumn + ");";
                            if (!foreignKeyCreate.containsKey(dao)) {
                                foreignKeyCreate.put(dao, new ArrayList<>());
                            }
                            foreignKeyCreate.get(dao).add(sql);
                        }
                    }
                }

                // Check schema
                PreparedQuery preparedQuery = dao.queryBuilder().orderBy("id", true).limit(1l).prepare();
                List<Object> list = dao.query(preparedQuery);
                if (list == null || list.size() > 1) {
                    throw new ApplicationInitializationException("Database error");
                }

            } catch (SQLException | TypedSqlException e) {
                throw new ApplicationInitializationException("Cannot create table for " + domainClass, e);
            }
        }

        for (Map.Entry<Dao, List<String>> entry : foreignKeyCreate.entrySet()) {
            for (String sql : entry.getValue()) {
                try {
                    entry.getKey().executeRaw(sql);
                    log.info("executed " + sql);
                } catch (SQLException e) {
                    throw new ApplicationInitializationException("Cannot create foreign key: " + entry.getValue(), e);
                }
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
