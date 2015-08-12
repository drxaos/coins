package com.github.drxaos.coins.application.web;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.factory.Component;
import com.github.drxaos.coins.application.events.ApplicationInit;
import com.github.drxaos.coins.application.events.ApplicationStop;
import spark.Spark;

@Component
public class Http implements ApplicationInit, ApplicationStop {

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {

        int maxThreads = 1 + Runtime.getRuntime().availableProcessors() * 2; // https://github.com/perwendel/spark/issues/306
        int minThreads = 2;
        int timeOutMillis = 5000;
        Spark.threadPool(maxThreads, minThreads, timeOutMillis);

        Spark.port(4567);

        Spark.staticFileLocation("static");
    }

    @Override
    public void onApplicationStop(Application application) throws ApplicationInitializationException {
        Spark.stop();
    }
}
