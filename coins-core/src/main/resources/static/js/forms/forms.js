angular.module('directive.forms', [])

    .directive('resetWithEsc', function () {
        return {
            restrict: 'A',
            require: '?ngModel',
            link: function (scope, element, attrs, controller) {
                element.on('keydown', function (ev) {
                    if (ev.keyCode != 27) return;

                    $(ev.target).closest("form").find("button[type=reset]").click();
                });
            },
        };
    })

    .directive('onEsc', function ($parse) {
        return {
            restrict: 'A',
            link: function ($scope, $element, $attr) {
                $element.on('keydown', function (ev) {
                    if (ev.keyCode != 27) return;
                    $scope.$apply($attr.onEsc);
                });
            }
        }
    })

    .directive('focusOn', function ($timeout) {
        return {
            restrict: 'A',
            link: function ($scope, $element, $attr) {
                $scope.$watch($attr.focusOn, function (_focusVal) {
                    $timeout(function () {
                        if (_focusVal) {
                            $element.focus().select();
                        } else {
                            $element.blur();
                        }
                    }, 100);
                });
            }
        }
    });
