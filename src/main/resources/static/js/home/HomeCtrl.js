function HomeCtrl($location, AuthService) {
    var model = this;

    AuthService.checkLoggedIn().then(function () {

        model.text = "This is Home module";


    });
}

InitializingModule.controller('HomeCtrl', HomeCtrl);
