function CategoriesRouter($routeProvider) {
    $routeProvider
        .when('/categories', {
            templateUrl: '../js/categories/categories.html',
            controller: 'CategoriesCtrl',
            controllerAs: 'categories',
            menuTitle: "CATEGORIES_MENU_TITLE",
            menuIcon: "ion-ios-filing fa-lg",
            headerTitle: "CATEGORIES_TOOLBAR_TITLE",
            menuGroup: "MENU_GROUP_MAIN",
        });
}

InitializingModule.config(CategoriesRouter);
