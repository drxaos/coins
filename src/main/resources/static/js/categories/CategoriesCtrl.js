function CategoriesCtrl(AuthService, CategoriesCollection, $rootScope) {
    var model = this;

    AuthService.checkLoggedIn().then(function () {

        model.text = "This is Categories module";

        return CategoriesCollection.query().$promise;
    }).then(function (entries) {
        model.count = entries.length;


        $rootScope.toolbarTools - [
            {
                name: "Search",
                icon: "ion-search fa-lg",
                click: function () {

                }
            },
            {
                name: "Settings",
                icon: "ion-android-more-vertical fa-lg",
                click: function () {

                }
            },

        ];
    });
}

InitializingModule.controller('CategoriesCtrl', CategoriesCtrl);
