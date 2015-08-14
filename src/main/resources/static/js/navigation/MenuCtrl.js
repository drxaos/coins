function MenuCtrl($rootScope, $route, $location, $mdSidenav) {
    var model = this;

    model.selected = null;

    model.menuLinks = {}
    for (key in $route.routes) {
        var r = $route.routes[key];
        if (r.menuTitle) {
            model.menuLinks[r.originalPath] = {title: r.menuTitle, icon: r.menuIcon}
        }
    }

    model.menuClick = menuClick;

    function toggleSidenav() {
        $mdSidenav('left').toggle();
    }

    function menuClick(href) {
        toggleSidenav('left');
    }

    $rootScope.$on('$locationChangeSuccess', function (event) {
        var url = $location.url(),
            params = $location.search();
        model.selected = url;
    })
}

InitializingModule.controller('MenuCtrl', MenuCtrl);
