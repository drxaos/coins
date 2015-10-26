package com.github.drxaos.coins.application.database.mysql.migration;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.config.ApplicationProps;

public class MigrationConfig extends ApplicationProps {

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {
        props.put("jdbc.url", "jdbc:mysql://localhost/coins?user=root&password=root");
        props.put("ref.jdbc.url", "jdbc:mysql://localhost/coins_tmp?user=root&password=root");
        props.put("ref.jdbc.dbName", "coins_tmp");
    }
}
