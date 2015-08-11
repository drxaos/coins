package com.github.drxaos.coins;


import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.ApplicationProps;
import com.github.drxaos.coins.application.Database;
import com.github.drxaos.coins.controller.JsonTransformer;
import com.github.drxaos.coins.controller.TxController;
import com.github.drxaos.coins.domain.*;
import com.github.drxaos.coins.service.user.PasswordService;
import com.github.drxaos.coins.utils.DateUtil;

public class Main {
    public static class Config extends ApplicationProps {

        @Override
        public void onApplicationInit(Application application) throws ApplicationInitializationException {

            props.put("jdbc.url", "jdbc:h2:./coins");

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
                        Database.class,
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
                        JsonTransformer.class,
                        TxController.class
                );

                // Services
                addObjects(
                        PasswordService.class
                );

                // Utils
                addObjects(
                        DateUtil.class
                );

            }
        };

        application.start();
        application.stop();
    }
}

