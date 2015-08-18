package com.github.drxaos.coins.application.web;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationInit;
import com.github.drxaos.coins.application.factory.Component;
import com.github.drxaos.coins.domain.User;
import spark.Spark;

import java.util.Arrays;

@Component
public class Security implements ApplicationInit {

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {

        // disable auth check on urls
        for (String path : Arrays.asList(

                "/",
                "/auth/*",
                "/ui/*",
                "/js/*",
                "/css/*",
                "/libs/*",
                "/templates/*"

        )) {
            Spark.before(path, (request, response) -> request.attribute("dont-check-auth", true));
        }

        // check auth filter
        Spark.before((request, response) -> {
            request.session(true);

            Object dontCheckAuth = request.attribute("dont-check-auth");
            if (dontCheckAuth != null && (boolean) dontCheckAuth) {
                return;
            }

            User user = request.session().attribute("user");
            if (user == null) {
                Spark.halt(401, "Unauthorized");
            }
        });

    }


}
