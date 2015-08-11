package com.github.drxaos.coins.application;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component(dependencies = {ApplicationProps.class})
public class DbConnection implements ApplicationInit, ApplicationStop {

    private ConnectionSource connectionSource;
    private Map<Class, Dao> daoMap = new HashMap<>();

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {
        ApplicationProps props = application.getFactory().findObject(ApplicationProps.class);
        String jdbcUrl = props.getString("jdbc.url", "jdbc:h2:mem:test");

        try {
            connectionSource = new JdbcConnectionSource(jdbcUrl);
        } catch (SQLException e) {
            throw new ApplicationInitializationException("Cannot connect to database", e);
        }
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public <T> Dao<T, Long> getDao(Class<T> domainCls) throws SQLException {
        if (!daoMap.containsKey(domainCls)) {
            daoMap.put(domainCls, DaoManager.createDao(connectionSource, domainCls));
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
