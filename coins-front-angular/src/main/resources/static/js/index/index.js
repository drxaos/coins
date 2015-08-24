require("../js/navigation/navigation.js");
require("../js/home/home.js");
require("../js/about/about.js");
require("../js/categories/categories.js");
require("../js/settings/settings.js");
require("../js/auth/auth.js");
require("../js/loading/loading.js");
require("../js/forms/forms.js");

InitializingModule = angular.module('Index', [
    'ngMaterial',
    'ngRoute',
    'pascalprecht.translate',

    'directive.loading',
    'directive.forms',
    'Navigation',
    'Auth',

    'Home',
    'Categories',

    'Settings',
    'About'
]);

InitializingModule.controller('IndexCtrl', function ($scope, $rootScope, $mdSidenav) {
    var model = this;

    $scope.$on('$viewContentLoaded', function () {
        angular.element(document.querySelector(".app-loading")).fadeOut();
    });

    model.openSidenav = function () {
        if (window.isMobile) {
            $mdSidenav('left').open();
        }
    };
    model.closeSidenav = function () {
        if (window.isMobile) {
            $mdSidenav('left').close();
        }
    };

    if (window.isMobile) {
        $("body").addClass("noselect");
    }

    $rootScope.toolbarTools = [];
    $rootScope.fab = {show: false};

});

require("../js/index/IndexI18n.js");
