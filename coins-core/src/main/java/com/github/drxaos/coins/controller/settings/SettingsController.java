package com.github.drxaos.coins.controller.settings;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationStart;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.controller.AbstractRestPublisher;
import com.github.drxaos.coins.controller.RestHandler;
import com.github.drxaos.coins.domain.User;
import com.github.drxaos.coins.service.settings.SettingsService;

public class SettingsController implements ApplicationStart {

    public static final String CONTEXT = "/api/v1/settings";

    @Inject
    AbstractRestPublisher publisher;

    @Override
    public void onApplicationStart(Application application) throws ApplicationInitializationException {
        publisher.publish(AbstractRestPublisher.Method.GET, CONTEXT, getSettings);
        publisher.publish(AbstractRestPublisher.Method.PUT, CONTEXT + "/lang", setLang);
    }

    @Inject
    SettingsService settingsService;

    @Autowire
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
