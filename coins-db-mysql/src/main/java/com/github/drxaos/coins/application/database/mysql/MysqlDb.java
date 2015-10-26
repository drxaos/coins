package com.github.drxaos.coins.application.database.mysql;

import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.config.ApplicationProps;
import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.factory.Inject;

public class MysqlDb extends Db {

    @Inject
    protected ApplicationProps props;

    public String getJdbcUrl() {
        return props.getString("jdbc.url", "jdbc:mysql://localhost/coins?user=root&password=root");
    }

    @Override
    protected void initConnection() throws ApplicationInitializationException {
        initConnection(getJdbcUrl());
    }
}
