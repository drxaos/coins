package com.github.drxaos.coins;


import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.config.ApplicationProps;
import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.database.H2Dialect;
import com.github.drxaos.coins.application.web.Http;
import com.github.drxaos.coins.controller.JsonTransformer;
import com.github.drxaos.coins.controller.TxController;
import com.github.drxaos.coins.controller.auth.AuthController;
import com.github.drxaos.coins.controller.category.CategoryController;
import com.github.drxaos.coins.controller.settings.SettingsController;
import com.github.drxaos.coins.domain.*;
import com.github.drxaos.coins.service.settings.SettingsService;
import com.github.drxaos.coins.service.user.AuthService;
import com.github.drxaos.coins.service.user.PasswordService;
import com.github.drxaos.coins.utils.DateUtil;
import spark.Spark;

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

                // Database
                addObjects(
                        Db.class,
                        H2Dialect.class,
                        InitialData.class
                );
                addClasses(
                        Account.class,
                        Category.class,
                        Tx.class,
                        User.class
                );

                // Web
                addObjects(
                        Http.class,
                        JsonTransformer.class,
                        AuthController.class,
                        TxController.class,
                        CategoryController.class,
                        SettingsController.class
                );

                // Services
                addObjects(
                        PasswordService.class,
                        AuthService.class,
                        SettingsService.class
                );

                // Utils
                addObjects(
                        DateUtil.class
                );

            }
        };

        application.start();
        System.out.println("Started: http://localhost:4567/ui/");

        application.stop();
    }
}

