function AboutCtrl(AuthService, $rootScope) {
    var model = this;

    AuthService.checkLoggedIn().then(function () {

        model.text = "ABOUT_TEXT";

        $rootScope.toolbarTools = [];
        $rootScope.fab = {show: false};
    });
}

InitializingModule.controller('AboutCtrl', AboutCtrl);
