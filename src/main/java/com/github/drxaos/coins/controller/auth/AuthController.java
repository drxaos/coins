package com.github.drxaos.coins.controller.auth;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationStart;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.controller.JsonTransformer;
import com.github.drxaos.coins.controller.SecureRoute;
import com.github.drxaos.coins.domain.User;
import com.github.drxaos.coins.service.user.AuthService;
import spark.Spark;

import java.util.ArrayList;
import java.util.List;

public class AuthController implements ApplicationStart {

    @Inject
    JsonTransformer json;

    @Inject
    AuthService authService;

    public static final String CONTEXT = "/api/v1/auth";

    @Autowire
    public final SecureRoute<AuthRequest, AuthResponse> signIn = new SecureRoute<AuthRequest, AuthResponse>() {
        @Override
        public AuthResponse handle() throws Exception {
            AuthRequest input = input(AuthRequest.class);

            if (input.user != null && input.password != null) {
                User user = authService.checkAuth(input.user, input.password);
                if (user != null) {
                    auth(user);
                    return new AuthResponse(true, "");
                }
            }

            return new AuthResponse(false, "wrong-credentials");
        }
    };

    @Autowire
    public final SecureRoute<Void, String> signOut = new SecureRoute<Void, String>() {
        @Override
        public String handle() throws Exception {
            auth(null);
            return "ok";
        }
    };

    @Autowire
    public final SecureRoute<Void, WhoamiResponse> whoAmI = new SecureRoute<Void, WhoamiResponse>() {
        @Override
        public WhoamiResponse handle() throws Exception {
            User user = loggedInUser();
            return new WhoamiResponse(
                    user != null ? user.name() : "",
                    user != null ? user.lang() : "en"
            );
        }
    };

    @Override
    public void onApplicationStart(Application application) throws ApplicationInitializationException {
        Spark.post(CONTEXT + "/sign_in", signIn, json);
        Spark.post(CONTEXT + "/sign_out", signOut, json);
        Spark.get(CONTEXT + "/whoami", whoAmI, json);
    }
}
