function SettingsRouter($routeProvider) {
    $routeProvider
        .when('/settings', {
            templateUrl: '../js/settings/settings.html',
            controller: 'SettingsCtrl',
            controllerAs: 'settings',
            menuTitle: "Settings",
            menuIcon: "ion-wrench fa-lg",
            headerTitle: "Settings",
            menuGroup: "settings",
        });
}

InitializingModule.config(SettingsRouter);
