function CategoriesCtrl(AuthService, CategoriesCollection) {
    var model = this;

    AuthService.checkLoggedIn().then(function () {

        model.text = "This is Categories module";

        return CategoriesCollection.query().$promise;
    }).then(function (entries) {
        model.count = entries.length;
    });
}

InitializingModule.controller('CategoriesCtrl', CategoriesCtrl);
