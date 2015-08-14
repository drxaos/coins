require("../js/navigation/navigation.js");
require("../js/home/home.js");
require("../js/about/about.js");

InitializingModule = angular.module('Index', ['ngMaterial', 'ngRoute', 'Navigation', 'Home', 'About']);

