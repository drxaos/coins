package com.github.drxaos.coins.controller.auth;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationStart;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.controller.AbstractRestPublisher;
import com.github.drxaos.coins.controller.RestHandler;
import com.github.drxaos.coins.domain.User;
import com.github.drxaos.coins.service.user.AuthService;

public class AuthController implements ApplicationStart {

    public static final String CONTEXT = "/api/v1/auth";

    @Inject
    AbstractRestPublisher publisher;

    @Override
    public void onApplicationStart(Application application) throws ApplicationInitializationException {
        publisher.publish(AbstractRestPublisher.Method.POST, CONTEXT + "/sign_in", signIn);
        publisher.publish(AbstractRestPublisher.Method.POST, CONTEXT + "/sign_out", signOut);
        publisher.publish(AbstractRestPublisher.Method.GET, CONTEXT + "/whoami", whoAmI);
    }

    @Inject
    AuthService authService;

    @Autowire
    public final RestHandler<AuthRequest, AuthResponse> signIn = new RestHandler<AuthRequest, AuthResponse>() {
        @Override
        public AuthResponse handle() throws Exception {
            AuthRequest input = transport.input(AuthRequest.class);

            if (input.user != null && input.password != null) {
                User user = authService.checkAuth(input.user, input.password);
                if (user != null) {
                    transport.auth(user);
                    return new AuthResponse(true, "");
                }
            }

            return new AuthResponse(false, "wrong-credentials");
        }
    };

    @Autowire
    public final RestHandler<Void, String> signOut = new RestHandler<Void, String>() {
        @Override
        public String handle() throws Exception {
            transport.auth(null);
            return "ok";
        }
    };

    @Autowire
    public final RestHandler<Void, WhoamiResponse> whoAmI = new RestHandler<Void, WhoamiResponse>() {
        @Override
        public WhoamiResponse handle() throws Exception {
            User user = transport.loggedInUser();
            return new WhoamiResponse(
                    user != null ? user.name() : "",
                    user != null ? user.lang() : "en"
            );
        }
    };

}
