package com.github.drxaos.coins.spark;


import com.github.drxaos.coins.CoinsCoreModule;
import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.config.ApplicationProps;
import com.github.drxaos.coins.application.database.mysql.CoinsDbMysqlModule;
import com.github.drxaos.coins.domain.InitialData;
import com.github.drxaos.coins.spark.components.JsonTransformer;
import com.github.drxaos.coins.spark.components.SparkPublisher;
import com.github.drxaos.coins.spark.config.Http;
import com.github.drxaos.coins.spark.config.Security;
import com.github.drxaos.coins.spark.sessions.DbSessionManager;
import spark.Spark;

public class Main {

    public static class Config extends ApplicationProps {

        @Override
        public void onApplicationInit(Application application) throws ApplicationInitializationException {

            props.put("jdbc.url", "jdbc:mysql://localhost/coins?user=root&password=root");

        }
    }

    public static void main(String[] args) throws ApplicationInitializationException {
        Application application = new Application() {
            @Override
            public void init() {

                // Config
                addObjects(
                        Config.class,
                        InitialData.class
                );

                // Web
                addObjects(
                        Http.class,
                        Security.class,
                        DbSessionManager.class,
                        JsonTransformer.class,
                        SparkPublisher.class
                );

                addClasses(CoinsCoreModule.TYPES);
                addObjects(CoinsCoreModule.COMPONENTS);

                addObjects(CoinsDbMysqlModule.COMPONENTS);
            }
        };

        application.start();
        Spark.awaitInitialization();

        System.out.println("Spark running: http://localhost:4567/");
    }
}
