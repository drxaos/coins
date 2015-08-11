package com.github.drxaos.coins.controller.auth;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.ApplicationStart;
import com.github.drxaos.coins.application.Autowire;
import com.github.drxaos.coins.controller.JsonTransformer;
import com.github.drxaos.coins.service.user.AuthService;
import spark.Spark;

public class AuthController implements ApplicationStart {

    @Autowire
    JsonTransformer json;

    @Autowire
    AuthService authService;

    @Override
    public void onApplicationStart(Application application) throws ApplicationInitializationException {
        Spark.before("/auth/*", (request, response) -> {
            request.attribute("dont-check-auth", true);
        });
        Spark.before("/", (request, response) -> {
            response.redirect("/ui/index.html");
        });
        Spark.before("/ui/*", (request, response) -> {
            request.attribute("dont-check-auth", true);
        });
        Spark.before((request, response) -> {
            request.session(true);

            Object dontCheckAuth = request.attribute("dont-check-auth");
            if (dontCheckAuth != null && (boolean) dontCheckAuth) {
                return;
            }

            String username = request.session().attribute("username");
            if (username == null) {
                Spark.halt(401, "Unauthorized");
            }
        });

        Spark.post("/auth/:name", (request, response) -> {

            String name = request.params(":name");
            if (authService.checkAuth(name, request.params("password"))) {
                request.session().attribute("user", name);
                return new AuthResponse(true, "");
            }

            return new AuthResponse(false, "wrong-credentials");
        }, json);

    }
}
