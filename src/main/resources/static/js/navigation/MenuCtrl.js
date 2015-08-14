function MenuCtrl($rootScope, $route, $location, $mdSidenav) {
    var model = this;

    model.selected = null;

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
        $mdSidenav('left').toggle();
    }

    function menuClick(href) {
        toggleSidenav('left');
    }

    function logout() {
        toggleSidenav('left');
        // TODO api/logout
        location.reload();
    }

    $rootScope.$on('$locationChangeSuccess', function (event) {
        var url = $location.url(),
            params = $location.search();
        model.selected = url;
    });

    model.username = "Username Username";
    model.email = "user@example.com";
}

InitializingModule.controller('MenuCtrl', MenuCtrl);
