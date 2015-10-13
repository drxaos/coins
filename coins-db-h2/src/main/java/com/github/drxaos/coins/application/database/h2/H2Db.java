package com.github.drxaos.coins.application.database.h2;

import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.config.ApplicationProps;
import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.factory.Inject;

public class H2Db extends Db {

    @Inject
    protected ApplicationProps props;

    public String getJdbcUrl() {
        return props.getString("jdbc.url", "jdbc:h2:mem:test");
    }

    @Override
    protected void initConnection() throws ApplicationInitializationException {
        initConnection(getJdbcUrl());
    }
}
