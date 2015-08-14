function ToolbarCtrl($rootScope, $location, $route, $mdSidenav) {
    var model = this;

    model.toggleSidenav = toggleSidenav;

    function toggleSidenav(name) {
        $mdSidenav(name).toggle();
    }

    model.title = "Project Coins";

    $rootScope.$on('$locationChangeSuccess', function (event) {
        var url = $location.url(),
            params = $location.search();
        var r = $route.routes[url];
        model.title = r.headerTitle;
    });
}

InitializingModule.controller('ToolbarCtrl', ToolbarCtrl);
