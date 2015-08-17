angular.module('directive.loading', [])

    .directive('loading', ['$http', function ($http, $window) {
        return {
            restrict: 'A',
            link: function (scope, elm, attrs) {
                scope.isLoading = function () {
                    return $http.pendingRequests.length > 0;
                };

                scope.$watch(scope.isLoading, function (v) {
                    if (v) {
                        var parent = $(elm[0]).parent();
                        var viewableTop = parent.scrollTop;
                        var viewableBottom = parent.innerHeight() + parent.scrollTop;
                        //getting child position within the parent
                        var childPos = $(elm[0]).position().top;
//getting difference between the childs top and parents viewable area
                        var yDiff = ($(elm[0]).position().top + $(elm[0]).outerHeight()) - parent.innerHeight();

                        //$(elm[0]).css('position', 'fixed');
                        //$(elm[0]).css('top', 0);

                        elm.fadeIn();
                    } else {
                        elm.fadeOut();
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