function AuthRouter($routeProvider) {
    $routeProvider
        .when('/login', {
            templateUrl: '../js/auth/auth.html',
            controller: 'AuthCtrl',
            controllerAs: 'auth',
            headerTitle: "Project Coins",
            auth: true,
        });
}

InitializingModule.config(AuthRouter);
