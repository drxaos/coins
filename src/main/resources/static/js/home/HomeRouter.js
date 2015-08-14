function HomeRouter($routeProvider) {
    $routeProvider
        .when('/home', {
            templateUrl: '../js/home/home.html',
            controller: 'HomeCtrl',
            controllerAs: 'home',
            menuTitle: "Home",
            menuIcon: "ion-ios-home fa-lg",
            headerTitle: "Project Coins",
            menuGroup: "main",
        })
        .otherwise({
            redirectTo: '/home'
        });
}

InitializingModule.config(HomeRouter);
