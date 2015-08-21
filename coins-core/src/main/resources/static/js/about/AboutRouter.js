function AboutRouter($routeProvider) {
    $routeProvider
        .when('/about', {
            templateUrl: '../js/about/about.html',
            controller: 'AboutCtrl',
            controllerAs: 'about',
            menuTitle: "ABOUT_MENU_TITLE",
            menuIcon: "ion-ios-people fa-lg",
            headerTitle: "ABOUT_TOOLBAR_TITLE",
            menuGroup: "MENU_GROUP_SETTINGS",
        });
}

InitializingModule.config(AboutRouter);
