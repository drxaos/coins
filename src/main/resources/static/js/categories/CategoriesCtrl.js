function CategoriesCtrl(AuthService, CategoriesCollection, $rootScope) {
    var model = this;

    AuthService.checkLoggedIn().then(function () {

        model.text = "This is Categories module";

        return CategoriesCollection.query().$promise;
    }).then(function (entries) {
        model.categories = entries;

        function search() {
        }

        function menu() {
        }

        model.search = {name: ""};

        $rootScope.toolbarTools = [
            {name: "Search", icon: "ion-search fa-lg", click: search},
            {name: "Settings", icon: "ion-android-more-vertical fa-lg", click: menu},
        ];
    });
}

InitializingModule.controller('CategoriesCtrl', CategoriesCtrl);
