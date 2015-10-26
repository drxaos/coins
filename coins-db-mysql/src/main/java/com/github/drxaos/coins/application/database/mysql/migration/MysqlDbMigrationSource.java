package com.github.drxaos.coins.application.database.mysql.migration;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.database.mysql.MysqlDb;
import com.github.drxaos.coins.application.factory.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MysqlDbMigrationSource extends MysqlDb {

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {
        log.info("opening target database: " + getJdbcUrl());
        setCheckSchema(false);
        super.onApplicationInit(application);
    }

}
