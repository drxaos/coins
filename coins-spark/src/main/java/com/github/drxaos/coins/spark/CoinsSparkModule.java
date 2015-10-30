package com.github.drxaos.coins.spark;


import com.github.drxaos.coins.spark.components.JsonTransformer;
import com.github.drxaos.coins.spark.components.SparkPublisher;
import com.github.drxaos.coins.spark.config.Http;
import com.github.drxaos.coins.spark.config.Security;
import com.github.drxaos.coins.spark.sessions.DbSessionManager;
import com.google.common.collect.ImmutableList;

import java.util.List;

public interface CoinsSparkModule {

    List<Class> COMPONENTS = ImmutableList.of(
            Http.class,
            Security.class,
            DbSessionManager.class,
            JsonTransformer.class,
            SparkPublisher.class
    );
}
