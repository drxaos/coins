package com.github.drxaos.coins.application.database.mysql;


import com.google.common.collect.ImmutableList;

import java.util.List;

public interface CoinsDbMysqlModule {

    List<Class> COMPONENTS = ImmutableList.of(
            MysqlDialect.class,
            MysqlDb.class
    );
}


