function SettingsCtrl(AuthService) {
    var model = this;

    AuthService.checkLoggedIn().then(function () {

        model.text = "This is Settings module";

    });
}

InitializingModule.controller('SettingsCtrl', SettingsCtrl);
