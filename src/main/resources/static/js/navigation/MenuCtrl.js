function MenuCtrl($rootScope, $route, $location, $mdSidenav, $mdToast, $http) {
    var model = this;

    model.template = '../js/navigation/menu.html';

    model.selected = null;

    model.username = "User1";
    model.email = "user1@example.com";

    model.menuLinks = {};
    for (key in $route.routes) {
        var r = $route.routes[key];
        if (r.menuTitle) {
            if (!model.menuLinks[r.menuGroup]) {
                model.menuLinks[r.menuGroup] = {};
            }
            model.menuLinks[r.menuGroup][r.originalPath] = {
                title: r.menuTitle,
                icon: r.menuIcon,
                group: r.menuGroup,
            };
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

    function updatemenu(event) {
        var url = $location.url(),
            params = $location.search();
        model.selected = url;
        var r = $route.routes[url];
        $rootScope.menuShow = r && !r.auth;
    }

    $rootScope.$on('$locationChangeSuccess', updatemenu);
    updatemenu();
}

InitializingModule.controller('MenuCtrl', MenuCtrl);
