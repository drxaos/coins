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
    });