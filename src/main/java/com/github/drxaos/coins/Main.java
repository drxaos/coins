package com.github.drxaos.coins;


import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.DbConnection;
import com.github.drxaos.coins.application.DbInitializer;
import com.github.drxaos.coins.controller.JsonTransformer;
import com.github.drxaos.coins.controller.TxController;
import com.github.drxaos.coins.domain.Account;
import com.github.drxaos.coins.domain.Category;
import com.github.drxaos.coins.domain.Tx;
import com.github.drxaos.coins.domain.User;

public class Main {
    public static void main(String[] args) throws ApplicationInitializationException {
        Application application = new Application() {
            @Override
            public void init() {
                addObjects(
                        Config.class,

                        // Database
                        DbConnection.class,
                        DbInitializer.class,

                        // Web
                        JsonTransformer.class,
                        TxController.class
                );

                addClasses(
                        Account.class,
                        Category.class,
                        Tx.class,
                        User.class
                );
            }
        };

        application.start();
        application.stop();
    }
}

