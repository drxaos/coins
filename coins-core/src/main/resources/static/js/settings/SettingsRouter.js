function SettingsRouter($routeProvider) {
    $routeProvider
        .when('/settings', {
            templateUrl: '../js/settings/settings.html',
            controller: 'SettingsCtrl',
            controllerAs: 'settings',
            menuTitle: "SETTINGS_MENU_TITLE",
            menuIcon: "ion-wrench fa-lg",
            headerTitle: "SETTINGS_TOOLBAR_TITLE",
            menuGroup: "MENU_GROUP_SETTINGS",
        });
}

InitializingModule.config(SettingsRouter);
