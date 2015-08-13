function RouterConfig($routeProvider) {
    $routeProvider
        .when('/home', {
            templateUrl: '../js/home/home.html',
            controller: 'HomeCtrl',
            controllerAs: 'home'
        })
        .when('/about', {
            templateUrl: '../js/about/about.html',
            controller: 'AboutCtrl',
            controllerAs: 'about'
        })
        .otherwise({
            redirectTo: '/home'
        });
}

InitializingModule.config(RouterConfig);
