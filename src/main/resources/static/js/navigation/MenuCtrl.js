function MenuCtrl($rootScope, $route, $location, $mdSidenav, $mdToast, $http) {
    var model = this;

    model.selected = null;

    model.username = "User1";
    model.email = "user1@example.com";

    model.menuLinks = {};
    var group = null;
    for (key in $route.routes) {
        var r = $route.routes[key];
        if (r.menuTitle) {
            var divider = false;
            if (group != null && group != r.menuGroup) {
                divider = true;
            }
            model.menuLinks[r.originalPath] = {title: r.menuTitle, icon: r.menuIcon, divider: divider};
            group = r.menuGroup;
        }
    }

    model.menuClick = menuClick;
    model.logout = logout;

    function toggleSidenav() {
        $mdSidenav('left').close();
    }

    function menuClick(href) {
        toggleSidenav();
    }

    function logout() {
        toggleSidenav();
        $http.post("/api/v1/auth/sign_out")
            .success(function () {
                location.reload();
            })
            .error(function () {
                $mdToast.show({
                    template: "<md-toast>Server error</md-toast>"
                });
            });
    }

    $rootScope.$on('$locationChangeSuccess', function (event) {
        var url = $location.url(),
            params = $location.search();
        model.selected = url;
        var r = $route.routes[url];
        model.show = !r.auth;
    });
}

InitializingModule.controller('MenuCtrl', MenuCtrl);
