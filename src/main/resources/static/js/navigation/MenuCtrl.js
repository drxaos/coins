function MenuCtrl($rootScope, $location, $mdSidenav) {
    var model = this;

    model.selected = null;
    model.menuLinks = {
        "/home": {title: "Home", icon: "home"},
        "/about": {title: "About", icon: "people"},
    };
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
