require("../js/navigation/navigation.js");
require("../js/home/home.js");
require("../js/about/about.js");
require("../js/categories/categories.js");
require("../js/settings/settings.js");

InitializingModule = angular.module('Index', ['ngMaterial', 'ngRoute', 'Navigation', 'Home', 'Categories', 'Settings', 'About']);

