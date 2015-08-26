package com.github.drxaos.coins.application.database.h2;


import com.google.common.collect.ImmutableList;

import java.util.List;

public interface CoinsDbH2Module {

    List<Class> COMPONENTS = ImmutableList.of(
            H2Dialect.class,
            H2Db.class
    );
}


