function CategoriesCtrl(AuthService, CategoriesCollection, $rootScope) {
    var model = this;

    AuthService.checkLoggedIn().then(function () {
        return CategoriesCollection.query().$promise;
    }).then(function (entries) {
        model.categories = entries;

        function search() {
        }

        function menu() {
        }

        model.search = {name: ""};

        $rootScope.toolbarTools = [
            {name: "CATEGORIES_SEARCH", icon: "ion-search fa-lg", click: search},
            {name: "CATEGORIES_MENU", icon: "ion-android-more-vertical fa-lg", click: menu},
        ];
    });
}

InitializingModule.controller('CategoriesCtrl', CategoriesCtrl);
