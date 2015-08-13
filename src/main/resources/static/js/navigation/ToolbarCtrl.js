function ToolbarCtrl($mdSidenav) {
    var model = this;

    model.toggleSidenav = toggleSidenav;

    function toggleSidenav(name) {
        $mdSidenav(name).toggle();
    }

    model.title = "Hello Angular.js!";
}

InitializingModule.controller('ToolbarCtrl', ToolbarCtrl);
