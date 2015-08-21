function HomeCtrl($location, AuthService, $rootScope) {
    var model = this;

    AuthService.checkLoggedIn().then(function () {

        model.text = "This is Home module";

        $rootScope.toolbarTools = [];
        $rootScope.fab = {show: false};

    });
}

InitializingModule.controller('HomeCtrl', HomeCtrl);
