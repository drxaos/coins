function SettingsCtrl(AuthService, $rootScope) {
    var model = this;

    AuthService.checkLoggedIn().then(function () {

        model.text = "This is Settings module";

        $rootScope.toolbarTools = [];
    });
}

InitializingModule.controller('SettingsCtrl', SettingsCtrl);
