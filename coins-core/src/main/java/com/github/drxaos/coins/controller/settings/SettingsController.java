package com.github.drxaos.coins.controller.settings;

import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.controller.*;
import com.github.drxaos.coins.domain.User;
import com.github.drxaos.coins.service.settings.SettingsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PublishingContext("/api/v1/settings")
public class SettingsController extends AbstractRestController {

    @Inject
    SettingsService settingsService;

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.GET, path = "")
    public final RestHandler<Void, SettingsResponse> getSettings = new RestHandler<Void, SettingsResponse>() {
        @Override
        public SettingsResponse handle() throws Exception {
            User user = transport.loggedInUser();
            if (user != null) {
                return new SettingsResponse().lang(user.lang());
            } else {
                return null;
            }
        }
    };

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.PUT, path = "/lang")
    public final RestHandler<SetLangRequest, String> setLang = new RestHandler<SetLangRequest, String>() {
        @Override
        public String handle() throws Exception {
            SetLangRequest input = transport.input(SetLangRequest.class);

            if (input.lang != null) {
                User user = transport.loggedInUser();
                if (user != null) {
                    user = settingsService.changeLang(user, input.lang);
                    transport.auth(user);
                }
            }
            return "done";
        }
    };

}
