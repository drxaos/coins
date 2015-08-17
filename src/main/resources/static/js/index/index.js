require("../js/navigation/navigation.js");
require("../js/home/home.js");
require("../js/about/about.js");
require("../js/categories/categories.js");
require("../js/settings/settings.js");
require("../js/auth/auth.js");
require("../js/loading/loading.js");

InitializingModule = angular.module('Index', [
    'ngMaterial',
    'ngRoute',
    'pascalprecht.translate',

    'directive.loading',
    'Navigation',
    'Auth',

    'Home',
    'Categories',

    'Settings',
    'About'
]);

InitializingModule.controller('IndexCtrl', function ($scope) {
    $scope.$on('$viewContentLoaded', function () {
        angular.element(document.querySelector(".app-loading")).fadeOut();
    });
});

require("../js/index/IndexI18n.js");
