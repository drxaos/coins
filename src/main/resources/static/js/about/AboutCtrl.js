function AboutCtrl(AuthService, $rootScope) {
    var model = this;

    AuthService.checkLoggedIn().then(function () {

        model.text = "This is About module";

        $rootScope.toolbarTools = [];
    });
}

InitializingModule.controller('AboutCtrl', AboutCtrl);