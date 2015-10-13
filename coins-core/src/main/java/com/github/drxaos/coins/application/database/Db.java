package com.github.drxaos.coins.application.database;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationInit;
import com.github.drxaos.coins.application.events.ApplicationStop;
import com.github.drxaos.coins.application.factory.AutowiringFactory;
import com.github.drxaos.coins.application.factory.Component;
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
public abstract class Db implements ApplicationInit, ApplicationStop {

    protected AutowiringFactory factory;

    protected JdbcConnectionSource connectionSource;
    protected Map<Class, Dao> daoMap = new HashMap<>();

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {
        factory = application.getFactory();
        Entity.factory = factory;

        initConnection();

        List<Class> domainClasses = application.getFactory().getClassesByAnnotation(DatabaseTable.class);
        Map<Dao, List<String>> postCheckSql = new HashMap<>();
        for (Class domainClass : domainClasses) {
            try {
                checkTable(domainClass, postCheckSql);
            } catch (SQLException | TypedSqlException e) {
                throw new ApplicationInitializationException("Cannot check table " + domainClass, e);
            }
        }

        for (Map.Entry<Dao, List<String>> entry : postCheckSql.entrySet()) {
            for (String sql : entry.getValue()) {
                try {
                    entry.getKey().executeRaw(sql);
                    log.info("executed " + sql);
                } catch (SQLException e) {
                    throw new ApplicationInitializationException("Cannot execute post check sql: " + entry.getValue(), e);
                }
            }
        }
    }

    protected boolean createSchema = false;

    public void setCreateSchema(boolean createSchema) {
        this.createSchema = createSchema;
    }

    public boolean isCreateSchema() {
        return createSchema;
    }

    protected boolean checkSchema = true;

    public boolean isCheckSchema() {
        return checkSchema;
    }

    public void setCheckSchema(boolean checkSchema) {
        this.checkSchema = checkSchema;
    }

    protected void checkTable(Class domainClass, Map<Dao, List<String>> postCreationSql) throws TypedSqlException, SQLException, ApplicationInitializationException {
        Dao dao = getDao(domainClass);
        if (!dao.isTableExists() && createSchema) {
            TableUtils.createTable(getConnectionSource(), domainClass);
            makeForeignKeyConstraints(dao, postCreationSql);
        }

        // Check schema
        if (checkSchema) {
            PreparedQuery preparedQuery = dao.queryBuilder().orderBy("id", true).limit(1l).prepare();
            List<Object> list = dao.query(preparedQuery);
            if (list == null || list.size() > 1) {
                throw new ApplicationInitializationException("Database error: got " + list + " from [" + preparedQuery + "]");
            }
        }
    }

    protected void makeForeignKeyConstraints(Dao dao, Map<Dao, List<String>> postCreationSql) {
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
                if (!postCreationSql.containsKey(dao)) {
                    postCreationSql.put(dao, new ArrayList<String>());
                }
                postCreationSql.get(dao).add(sql);
            }
        }
    }

    protected abstract void initConnection() throws ApplicationInitializationException;

    protected void initConnection(String jdbcUrl) throws ApplicationInitializationException {
        try {
            connectionSource = new JdbcConnectionSource(jdbcUrl);
        } catch (SQLException e) {
            throw new ApplicationInitializationException("Cannot connect to database", e);
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
