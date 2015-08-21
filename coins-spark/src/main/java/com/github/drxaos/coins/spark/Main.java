package com.github.drxaos.coins.spark;


import com.github.drxaos.coins.CoinsCoreModule;
import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.config.ApplicationProps;
import com.github.drxaos.coins.spark.components.JsonTransformer;
import com.github.drxaos.coins.spark.components.SparkPublisher;
import com.github.drxaos.coins.spark.config.Http;
import com.github.drxaos.coins.spark.config.Security;

public class Main {
    public static class Config extends ApplicationProps {

        @Override
        public void onApplicationInit(Application application) throws ApplicationInitializationException {

            props.put("jdbc.url", "jdbc:h2:./coins;database_To_Upper=false");

        }
    }

    public static void main(String[] args) throws ApplicationInitializationException {
        Application application = new Application() {
            @Override
            public void init() {

                // Config
                addObjects(
                        Config.class
                );

                // Web
                addObjects(
                        Http.class,
                        Security.class,
                        JsonTransformer.class,
                        SparkPublisher.class
                );

                CoinsCoreModule.init(this);
            }
        };

        application.start();
    }
}

