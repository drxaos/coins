package com.github.drxaos.coins.controller.auth;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationStart;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.controller.JsonTransformer;
import com.github.drxaos.coins.domain.User;
import com.github.drxaos.coins.service.user.AuthService;
import spark.Spark;

public class AuthController implements ApplicationStart {

    @Autowire
    JsonTransformer json;

    @Autowire
    AuthService authService;

    public static final String CONTEXT = "/api/v1/auth";

    @Override
    public void onApplicationStart(Application application) throws ApplicationInitializationException {

        Spark.post(CONTEXT + "/sign_in", (request, response) -> {
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

        Spark.post(CONTEXT + "/sign_out", (request, response) -> {
            request.session().attribute("user", null);
            return "ok";
        }, json);

        Spark.get(CONTEXT + "/whoami", (request, response) -> {
            User user = request.session().attribute("user");
            return new WhoamiResponse(user != null ? user.name() : "");
        }, json);

    }
}
