package com.github.drxaos.coins.controller.auth;

import com.github.drxaos.coins.application.controller.RestError;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.application.validation.ValidationResult;
import com.github.drxaos.coins.controller.*;
import com.github.drxaos.coins.domain.User;
import com.github.drxaos.coins.errors.CheckPasswordException;
import com.github.drxaos.coins.service.user.AuthService;

@PublishingContext("/api/v1/auth")
public class AuthController extends AbstractRestController {

    @Inject
    AuthService authService;

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.POST, path = "/sign_in")
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
    @Publish(method = AbstractRestPublisher.Method.POST, path = "/sign_out")
    public final RestHandler<Void, String> signOut = new RestHandler<Void, String>() {
        @Override
        public String handle() throws Exception {
            transport.auth(null);
            return "ok";
        }
    };

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.GET, path = "/whoami")
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

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.POST, path = "/password")
    public final RestHandler<ChangePasswordRequest, Object> changePassword = new RestHandler<ChangePasswordRequest, Object>() {
        @Override
        public Object handle() throws Exception {
            ChangePasswordRequest input = transport.input(ChangePasswordRequest.class);
            User user = transport.loggedInUser();

            ValidationResult<ChangePasswordRequest> vr = input.validate();
            if (vr.hasErrors()) {
                return new RestError("field-error", vr);
            }

            try {
                authService.changePassword(user, input.oldPassword, input.newPassword);
            } catch (CheckPasswordException e) {
                vr.fieldError("oldPassword", "incorrect");
                return new RestError("field-error", vr);
            }
            return new ChangePasswordResponse(true);
        }
    };

}
