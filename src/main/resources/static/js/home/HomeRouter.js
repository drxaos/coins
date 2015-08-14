function HomeRouter($routeProvider) {
    $routeProvider
        .when('/home', {
            templateUrl: '../js/home/home.html',
            controller: 'HomeCtrl',
            controllerAs: 'home',
            menuTitle: "Home",
            menuIcon: "home"
        })
        .otherwise({
            redirectTo: '/home'
        });
}

InitializingModule.config(HomeRouter);
