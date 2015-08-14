function AboutRouter($routeProvider) {
    $routeProvider
        .when('/about', {
            templateUrl: '../js/about/about.html',
            controller: 'AboutCtrl',
            controllerAs: 'about',
            menuTitle: "About",
            menuIcon: "people"
        });
}

InitializingModule.config(AboutRouter);
