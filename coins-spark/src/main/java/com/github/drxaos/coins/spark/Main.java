package com.github.drxaos.coins.spark;


import com.github.drxaos.coins.CoinsCoreModule;
import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.config.ApplicationProps;
import com.github.drxaos.coins.application.database.h2.CoinsDbH2Module;
import com.github.drxaos.coins.domain.InitialData;
import com.github.drxaos.coins.spark.components.JsonTransformer;
import com.github.drxaos.coins.spark.components.SparkPublisher;
import com.github.drxaos.coins.spark.config.Http;
import com.github.drxaos.coins.spark.config.Security;
import com.github.drxaos.coins.spark.sessions.DbSessionManager;
import com.github.drxaos.coins.spark.sessions.StoredSession;
import spark.Spark;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.TimeZone;

public class Main {

    static {
        // force default encoding
        try {
            String property = System.getProperty("file.encoding");
            if (property != null && property.toUpperCase().equals("UTF-8")) {
                System.setProperty("file.encoding", "UTF-8");
                Field charset = Charset.class.getDeclaredField("defaultCharset");
                charset.setAccessible(true);
                charset.set(null, null);
            }
        } catch (Exception e) {
            //skip
        }

        // force UTC timezone
        try {
            System.setProperty("user.timezone", "UTC");
            TimeZone.setDefault(null);
        } catch (Exception e) {
            //skip
        }
    }

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

                addClasses(
                        StoredSession.class
                );

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

                addObjects(CoinsDbH2Module.COMPONENTS);
            }
        };

        application.start();
        Spark.awaitInitialization();

        System.out.println("Spark running: http://localhost:4567/");
    }
}

