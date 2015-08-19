angular.module('directive.loading', [])

    .directive('loading', ['$http', function ($http, $window) {
        return {
            restrict: 'A',
            link: function (scope, elm, attrs) {
                scope.isLoading = function () {
                    return $http.pendingRequests.length > 0;
                };

                var first = true;
                var delayed = null;
                scope.$watch(scope.isLoading, function (v) {
                    if (v) {
                        if (!first && delayed == null) {
                            delayed = setTimeout(function () {
                                delayed = null;
                                elm.fadeIn();
                            }, 800);
                        }
                    } else {
                        if (delayed == null) {
                            elm.fadeOut();
                            if (first) {
                                first = false;
                                elm.removeClass("app-loading");
                            }
                        } else {
                            clearTimeout(delayed);
                        }
                    }
                });
            }
        };

    }])

    .directive('spinner', function () {
        return {
            restrict: 'E',
            templateUrl: "../js/loading/spinner.html"
        };
    });