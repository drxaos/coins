function HomeCtrl($location, AuthService, $rootScope) {
    var model = this;

    AuthService.checkLoggedIn().then(function () {

        model.text = "This is Home module";

        $rootScope.toolbarTools = [];

    });
}

InitializingModule.controller('HomeCtrl', HomeCtrl);
