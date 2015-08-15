function AuthCtrl($scope, $http, AuthService, $location) {
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
}

InitializingModule.controller('AuthCtrl', AuthCtrl);
