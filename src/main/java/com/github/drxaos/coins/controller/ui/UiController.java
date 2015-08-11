package com.github.drxaos.coins.controller.ui;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.ApplicationStart;
import com.github.drxaos.coins.application.Autowire;
import com.github.drxaos.coins.controller.JsonTransformer;
import com.github.drxaos.coins.controller.auth.AuthController;
import com.github.drxaos.coins.controller.auth.AuthResponse;
import com.github.drxaos.coins.service.user.AuthService;
import spark.Spark;

public class UiController implements ApplicationStart {

    @Autowire
    AuthController authController;

    @Override
    public void onApplicationStart(Application application) throws ApplicationInitializationException {
        Spark.before("/", (request, response) -> {
            response.redirect("/ui/");
        });

    }
}
