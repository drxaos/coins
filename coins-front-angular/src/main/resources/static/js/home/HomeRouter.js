function HomeRouter($routeProvider) {
    $routeProvider
        .when('/home', {
            templateUrl: '../js/home/home.html',
            controller: 'HomeCtrl',
            controllerAs: 'home',
            menuTitle: "HOME_MENU_TITLE",
            menuIcon: "ion-ios-home fa-lg",
            headerTitle: "HOME_TOOLBAR_TITLE",
            menuGroup: "MENU_GROUP_MAIN",
        })
        .otherwise({
            redirectTo: '/home'
        });
}

InitializingModule.config(HomeRouter);
