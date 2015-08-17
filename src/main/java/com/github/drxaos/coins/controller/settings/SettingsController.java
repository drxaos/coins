package com.github.drxaos.coins.controller.settings;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationStart;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.controller.JsonTransformer;
import com.github.drxaos.coins.domain.User;
import com.github.drxaos.coins.service.settings.SettingsService;
import spark.Spark;

public class SettingsController implements ApplicationStart {

    @Autowire
    JsonTransformer json;

    @Autowire
    SettingsService settingsService;

    public static final String CONTEXT = "/api/v1/settings";

    @Override
    public void onApplicationStart(Application application) throws ApplicationInitializationException {

        Spark.get(CONTEXT, (request, response) -> {
            User user = request.session().attribute("user");
            if (user != null) {
                return new SettingsResponse().lang(user.lang());
            } else {
                return "";
            }
        }, json);


        Spark.put(CONTEXT + "/lang", (request, response) -> {
            SetLangRequest req = json.parse(request.body(), SetLangRequest.class);

            if (req.lang != null) {
                User user = request.session().attribute("user");
                if (user != null) {
                    user = settingsService.changeLang(user, req.lang);
                    request.session().attribute("user", user);
                }
            }

            return "done";
        }, json);

        Spark.get(CONTEXT + "/lang", (request, response) -> {
            User user = request.session().attribute("user");
            if (user != null) {
                return new GetLangResponse(user.lang());
            } else {
                return "";
            }
        }, json);
    }
}
