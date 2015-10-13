package com.github.drxaos.coins.application.database.h2.migration;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.database.h2.H2Db;
import com.github.drxaos.coins.application.factory.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class H2DbMigrationSource extends H2Db {

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {
        log.info("opening target database: " + getJdbcUrl());
        setCheckSchema(false);
        super.onApplicationInit(application);
    }

}
