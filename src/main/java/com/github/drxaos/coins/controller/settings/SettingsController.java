package com.github.drxaos.coins.controller.settings;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationStart;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.controller.JsonTransformer;
import com.github.drxaos.coins.controller.SecureRoute;
import com.github.drxaos.coins.domain.User;
import com.github.drxaos.coins.service.settings.SettingsService;
import spark.Spark;

public class SettingsController implements ApplicationStart {

    @Inject
    JsonTransformer json;

    @Inject
    SettingsService settingsService;

    public static final String CONTEXT = "/api/v1/settings";

    @Autowire
    public final SecureRoute<Void, SettingsResponse> getSettings = new SecureRoute<Void, SettingsResponse>() {
        @Override
        public SettingsResponse handle() throws Exception {
            User user = loggedInUser();
            if (user != null) {
                return new SettingsResponse().lang(user.lang());
            } else {
                return null;
            }
        }
    };

    @Autowire
    public final SecureRoute<SetLangRequest, String> setLang = new SecureRoute<SetLangRequest, String>() {
        @Override
        public String handle() throws Exception {
            SetLangRequest input = input(SetLangRequest.class);

            if (input.lang != null) {
                User user = loggedInUser();
                if (user != null) {
                    user = settingsService.changeLang(user, input.lang);
                    auth(user);
                }
            }
            return "done";
        }
    };

    @Override
    public void onApplicationStart(Application application) throws ApplicationInitializationException {
        Spark.get(CONTEXT, getSettings, json);
        Spark.put(CONTEXT + "/lang", setLang, json);
    }
}
