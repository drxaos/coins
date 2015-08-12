package com.github.drxaos.coins.controller.ui;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationStart;
import spark.Spark;

public class UiController implements ApplicationStart {

    @Override
    public void onApplicationStart(Application application) throws ApplicationInitializationException {
        Spark.before("/", (request, response) -> {
            response.redirect("/ui/");
        });

    }
}
