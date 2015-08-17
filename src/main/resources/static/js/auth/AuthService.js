function AuthError(message) {
    this.message = message;
}
AuthError.prototype = new Error;

function AuthService($mdToast, $http, $location, $q, $translate) {
    var svc = this;

    svc.user = null;

    function login(username, password, callback) {
        $http.post("/api/v1/auth/sign_in", {user: username, password: password})
            .success(function (data) {
                if (data.success) {
                    svc.user = username;
                    callback();
                } else {
                    $mdToast.show({
                        template: "<md-toast>" + data.error + "</md-toast>"
                    });
                }
            })
            .error(function () {
                $mdToast.show({
                    template: "<md-toast>Server error</md-toast>"
                });
            });
    }

    function isLoggedIn() {
        return $q(function (yes, no) {
            $http.get("/api/v1/auth/whoami")
                .success(function (data) {
                    svc.user = data.username;
                    if (data.lang != null) {
                        $translate.use(data.lang);
                    }
                    if (svc.user) {
                        yes(svc.user);
                    } else {
                        no(svc.user);
                    }
                })
                .error(function () {
                    svc.user = "";
                    no(svc.user);
                });
        });
    }

    function checkLoggedIn() {
        return $q(function (success) {
            isLoggedIn()
                .then(function () {
                    success();
                },
                function () {
                    $location.path("/login");
                    throw new AuthError("not-authorized");
                });
        });
    }

    return {
        isLoggedIn: isLoggedIn,
        checkLoggedIn: checkLoggedIn,
        login: login,
    }

}

InitializingModule.factory('AuthService', AuthService);
