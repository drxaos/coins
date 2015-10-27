package com.github.drxaos.coins.spark.config;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationInitEventListener;
import com.github.drxaos.coins.application.factory.Component;
import com.github.drxaos.coins.domain.User;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Arrays;

@Component(dependencies = {Http.class})
public class Security implements ApplicationInitEventListener {

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {

        // disable auth check on urls
        for (String path : Arrays.asList(

                "/",
                "/favicon.ico",
                "/webjars/*",
                "/api/v1/auth/*",
                "/ui",
                "/ui/*",
                "/img",
                "/img/*",
                "/js/*",
                "/css/*",
                "/libs/*",
                "/templates/*"

        )) {
            Spark.before(path, new Filter() {
                @Override
                public void handle(Request request, Response response) throws Exception {
                    request.attribute("dont-check-auth", true);
                }
            });
        }

        // check auth filter
        Spark.before(new Filter() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                request.session(true);

                Object dontCheckAuth = request.attribute("dont-check-auth");
                if (dontCheckAuth != null && (boolean) dontCheckAuth) {
                    return;
                }

                User user = request.session().attribute("user");
                if (user == null) {
                    Spark.halt(401, "Unauthorized");
                }
            }
        });

    }


}
