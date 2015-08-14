function AboutRouter($routeProvider) {
    $routeProvider
        .when('/about', {
            templateUrl: '../js/about/about.html',
            controller: 'AboutCtrl',
            controllerAs: 'about',
            menuTitle: "About",
            menuIcon: "ion-ios-people fa-lg",
            headerTitle: "About Coins",
            menuGroup: "settings",
        });
}

InitializingModule.config(AboutRouter);
