function AuthCtrl($scope, $http, AuthService, $location, $rootScope) {
    var model = this;

    // todo check auth

    model.login = login;
    model.username = "";
    model.password = "";

    function login() {
        AuthService.login(model.username, model.password, function () {
            $location.path("/");
        });
    }

    $rootScope.toolbarTools = [];
    $rootScope.fab = {show: false};
}

InitializingModule.controller('AuthCtrl', AuthCtrl);
