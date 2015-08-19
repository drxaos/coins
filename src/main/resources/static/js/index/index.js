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

InitializingModule.controller('IndexCtrl', function ($scope, $rootScope) {
    var model = this;

    $scope.$on('$viewContentLoaded', function () {
        angular.element(document.querySelector(".app-loading")).fadeOut();
    });

    $rootScope.toolbarTools = [];
    $rootScope.fab = {show: false};

});

require("../js/index/IndexI18n.js");
