package com.github.drxaos.coins.application;

import spark.Spark;

@Component
public class Http implements ApplicationInit, ApplicationStop {

    @Autowire
    ApplicationProps props;

    private AutowiringFactory factory;

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {
        factory = application.getFactory();

        int maxThreads = 8;
        int minThreads = 2;
        int timeOutMillis = 30000;
        Spark.threadPool(maxThreads, minThreads, timeOutMillis);

        Spark.port(4567);

        Spark.staticFileLocation("static");
    }

    @Override
    public void onApplicationStop(Application application) throws ApplicationInitializationException {
        Spark.stop();
    }
}
