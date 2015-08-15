function AboutCtrl(AuthService) {
    var model = this;

    AuthService.checkLoggedIn().then(function () {

        model.text = "This is About module";

    });
}

InitializingModule.controller('AboutCtrl', AboutCtrl);
