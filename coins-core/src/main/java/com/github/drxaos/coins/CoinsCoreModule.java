package com.github.drxaos.coins;


import com.github.drxaos.coins.controller.auth.AuthController;
import com.github.drxaos.coins.controller.category.CategoryController;
import com.github.drxaos.coins.controller.settings.SettingsController;
import com.github.drxaos.coins.domain.Account;
import com.github.drxaos.coins.domain.Category;
import com.github.drxaos.coins.domain.Tx;
import com.github.drxaos.coins.domain.User;
import com.github.drxaos.coins.service.settings.SettingsService;
import com.github.drxaos.coins.service.user.AuthService;
import com.github.drxaos.coins.service.user.PasswordService;
import com.github.drxaos.coins.utils.DateUtil;
import com.google.common.collect.ImmutableList;

import java.util.List;

public interface CoinsCoreModule {

    List<Class> TYPES = ImmutableList.of(
            // Database
            Account.class,
            Category.class,
            Tx.class,
            User.class
    );

    List<Class> COMPONENTS = ImmutableList.of(
            // Controllers
            AuthController.class,
            CategoryController.class,
            SettingsController.class,

            // Services
            PasswordService.class,
            AuthService.class,
            SettingsService.class,

            // Utils
            DateUtil.class
    );
}


