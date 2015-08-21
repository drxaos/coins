package com.github.drxaos.coins;


import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.database.H2Dialect;
import com.github.drxaos.coins.controller.auth.AuthController;
import com.github.drxaos.coins.controller.category.CategoryController;
import com.github.drxaos.coins.controller.settings.SettingsController;
import com.github.drxaos.coins.domain.*;
import com.github.drxaos.coins.service.settings.SettingsService;
import com.github.drxaos.coins.service.user.AuthService;
import com.github.drxaos.coins.service.user.PasswordService;
import com.github.drxaos.coins.utils.DateUtil;

public class CoinsCoreModule {

    public static void init(Application application) {

        // Database
        application.addObjects(
                Db.class,
                H2Dialect.class,
                InitialData.class
        );
        application.addClasses(
                Account.class,
                Category.class,
                Tx.class,
                User.class
        );

        // Web
        application.addObjects(
                AuthController.class,
                CategoryController.class,
                SettingsController.class
        );

        // Services
        application.addObjects(
                PasswordService.class,
                AuthService.class,
                SettingsService.class
        );

        // Utils
        application.addObjects(
                DateUtil.class
        );

    }
}


