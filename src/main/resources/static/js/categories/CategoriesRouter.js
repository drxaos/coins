function CategoriesRouter($routeProvider) {
    $routeProvider
        .when('/categories', {
            templateUrl: '../js/categories/categories.html',
            controller: 'CategoriesCtrl',
            controllerAs: 'categories',
            menuTitle: "Categories",
            menuIcon: "ion-ios-filing fa-lg",
            headerTitle: "Transaction categories",
            menuGroup: "main",
        });
}

InitializingModule.config(CategoriesRouter);
