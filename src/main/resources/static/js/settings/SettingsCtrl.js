function SettingsCtrl(AuthService, $rootScope, $translate, $http) {
    var model = this;

    AuthService.checkLoggedIn()
        .then(function () {
            return $http.get("/api/v1/settings");
        })
        .then(function (settings) {
            $rootScope.toolbarTools = [];

            model.langs = [
                {name: "ru", title: "Русский"},
                {name: "en", title: "English"},
            ];

            model.lang = settings.data.lang;

            model.updateLang = function () {
                $translate.use(model.lang);
                $http.put("/api/v1/settings/lang", {lang: model.lang});
            }
        });
}

InitializingModule.controller('SettingsCtrl', SettingsCtrl);
