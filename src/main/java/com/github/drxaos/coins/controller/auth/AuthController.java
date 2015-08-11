package com.github.drxaos.coins.controller.auth;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.ApplicationStart;
import com.github.drxaos.coins.application.Autowire;
import com.github.drxaos.coins.controller.JsonTransformer;
import com.github.drxaos.coins.domain.User;
import com.github.drxaos.coins.service.user.AuthService;
import spark.Spark;

import java.util.Arrays;

public class AuthController implements ApplicationStart {

    @Autowire
    JsonTransformer json;

    @Autowire
    AuthService authService;

    @Override
    public void onApplicationStart(Application application) throws ApplicationInitializationException {

        // disable auth check on urls
        for (String path : Arrays.asList("/", "/auth/*", "/ui/*")) {
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

        // auth
        Spark.post("/auth/sign_in", (request, response) -> {
            AuthRequest req = json.parse(request.body(), AuthRequest.class);

            if (req.user != null && req.password != null) {
                User user = authService.checkAuth(req.user, req.password);
                if (user != null) {
                    request.session().attribute("user", user);
                    return new AuthResponse(true, "");
                }
            }

            return new AuthResponse(false, "wrong-credentials");
        }, json);

        Spark.post("/auth/sign_out", (request, response) -> {
            request.session().attribute("user", null);
            return "ok";
        }, json);

        Spark.get("/auth/whoami", (request, response) -> {
            User user = request.session().attribute("user");
            return user != null ? user.name() : "anonymous";
        }, json);

    }
}
