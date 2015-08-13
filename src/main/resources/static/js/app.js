var app = angular.module('Application', ['ngMaterial']);

app.controller('MenuCtrl', ['$scope', '$location', '$mdSidenav', function ($scope, $location, $mdSidenav) {
    $scope.selected = null;
    $scope.menuLinks = [
        {id: 0, name: "Home", href: "/index", icon: "home"},
        {id: 1, name: "About", href: "/about", icon: "people"},
    ];
    $scope.selectPage = selectPage;
    $scope.toggleSidenav = toggleSidenav;

    function toggleSidenav(name) {
        $mdSidenav(name).toggle();
    }

    function selectPage(page) {
        $scope.selected = angular.isNumber(page) ? $scope.menuLinks[page] : page;
        $scope.toggleSidenav('left');
        $location.path($scope.selected.href);
    }

    for (link in $scope.menuLinks) {
        if (link.path == $location.path) {
            selectPage(link);
            break;
        }
    }
}]);