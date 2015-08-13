function MenuCtrl($location, $mdSidenav) {
    var model = this;

    model.selected = null;
    model.menuLinks = [
        {id: 0, name: "Home", href: "/home", icon: "home"},
        {id: 1, name: "About", href: "/about", icon: "people"},
    ];
    model.selectPage = selectPage;

    function toggleSidenav(name) {
        $mdSidenav(name).toggle();
    }

    function selectPage(page) {
        model.selected = angular.isNumber(page) ? model.menuLinks[page] : page;
        toggleSidenav('left');
        $location.path(model.selected.href);
    }

    for (link in model.menuLinks) {
        if (link.path == $location.path) {
            selectPage(link);
            break;
        }
    }
}

InitializingModule.controller('MenuCtrl', MenuCtrl);
