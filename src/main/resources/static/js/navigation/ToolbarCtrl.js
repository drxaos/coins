function ToolbarCtrl($rootScope, $location, $route, $mdSidenav) {
    var model = this;

    model.template = '../js/navigation/toolbar.html';

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
        model.show = !r.auth;
    });

    model.show = true;
}

InitializingModule.controller('ToolbarCtrl', ToolbarCtrl);
