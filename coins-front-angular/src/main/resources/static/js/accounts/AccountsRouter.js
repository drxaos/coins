function AccountsRouter($routeProvider) {
    $routeProvider
        .when('/accounts', {
            templateUrl: '../js/accounts/accounts.html',
            controller: 'AccountsCtrl',
            controllerAs: 'accounts',
            menuTitle: "ACCOUNTS_MENU_TITLE",
            menuIcon: "ion-cash fa-lg",
            headerTitle: "ACCOUNTS_TOOLBAR_TITLE",
            menuGroup: "MENU_GROUP_MAIN",
        });
}

InitializingModule.config(AccountsRouter);
