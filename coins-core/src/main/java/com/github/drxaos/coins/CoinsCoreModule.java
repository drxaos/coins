package com.github.drxaos.coins;


import com.github.drxaos.coins.controller.account.AccountController;
import com.github.drxaos.coins.controller.auth.AuthController;
import com.github.drxaos.coins.controller.category.CategoryController;
import com.github.drxaos.coins.controller.settings.SettingsController;
import com.github.drxaos.coins.controller.transactions.ChartsController;
import com.github.drxaos.coins.controller.transactions.TxController;
import com.github.drxaos.coins.domain.*;
import com.github.drxaos.coins.service.chart.ChartService;
import com.github.drxaos.coins.service.settings.SettingsService;
import com.github.drxaos.coins.service.tx.TxService;
import com.github.drxaos.coins.service.user.AuthService;
import com.github.drxaos.coins.service.user.PasswordService;
import com.github.drxaos.coins.utils.DateUtil;
import com.google.common.collect.ImmutableList;

import java.util.List;

public interface CoinsCoreModule {
    StartupFixes STARTUP_FIXES = new StartupFixes();

    List<Class> TYPES = ImmutableList.of(
            // Database
            Account.class,
            Category.class,
            Tx.class,
            User.class,
            Session.class,
            Option.class
    );

    List<Class> COMPONENTS = ImmutableList.of(
            // Controllers
            AuthController.class,
            CategoryController.class,
            AccountController.class,
            SettingsController.class,
            ChartsController.class,
            TxController.class,

            // Services
            PasswordService.class,
            AuthService.class,
            SettingsService.class,
            TxService.class,
            ChartService.class,

            // Utils
            DateUtil.class
    );
}
