function CategoriesCtrl(AuthService, CategoryEntity, $rootScope, $mdToast, $translate) {
    var model = this;

    AuthService.checkLoggedIn().then(function () {
        return CategoryEntity.query().$promise;
    }).then(function (entries) {

        // list

        model.list = entries;

        model.updateEntity = function (item) {
            if (item.newEntity) {
                return;
            }
            item.$update(function (res) {
                if (res.success) {
                    $mdToast.show({
                        template: "<md-toast>{{'CATEGORIES_SUCCESS_" + res.success + "'|translate}}</md-toast>"
                    });
                }
            }, function (res) {
                if (res.data.error) {
                    $mdToast.show({
                        template: "<md-toast>{{'CATEGORIES_ERROR_" + res.data.error + "'|translate}}</md-toast>"
                    });
                }
            });
        };

        model.deleteEntity = function (item) {
            item.$delete(function (res) {
                if (res.success) {
                    $mdToast.show({
                        template: "<md-toast>{{'CATEGORIES_SUCCESS_" + res.success + "'|translate}}</md-toast>"
                    });
                }
            }, function (res) {
                if (res.data.error) {
                    $mdToast.show({
                        template: "<md-toast>{{'CATEGORIES_ERROR_" + res.data.error + "'|translate}}</md-toast>"
                    });
                }
            });
        };

        model.addEntity = function () {
            for (e in model.list) {
                if (model.list[e].newEntity) {
                    $(".categories-new-item").find("input").focus().select();
                    return;
                }
            }
            model.list.push(new CategoryEntity({
                name: "",
                expense: false,
                income: false,
                newEntity: true
            }));
        };

        model.saveEntity = function (item) {
            CategoryEntity.save(item, function (saved) {
                model.list[model.list.indexOf(item)] = saved;
                $mdToast.show({
                    template: "<md-toast>{{'CATEGORIES_SUCCESS_entity-saved'|translate}}</md-toast>"
                });
            }, function (res) {
                if (res.data.error) {
                    $mdToast.show({
                        template: "<md-toast>{{'CATEGORIES_ERROR_" + res.data.error + "'|translate}}</md-toast>"
                    });
                }
            });
        };

        model.cancelEntity = function (item) {
            model.list.splice(model.list.indexOf(item), 1);
        };

        // menu

        function search() {
        }

        function menu() {
        }

        model.search = {name: ""};

        $rootScope.toolbarTools = [
            {name: "CATEGORIES_SEARCH", icon: "ion-search fa-lg", click: search},
            {name: "CATEGORIES_MENU", icon: "ion-android-more-vertical fa-lg", click: menu},
        ];
        $rootScope.fab = {
            show: true,
            title: "Add",
            icon: "ion-plus-round fa-lg",
            click: function (evt) {
                model.addEntity();
            }
        };
    });
}

InitializingModule.controller('CategoriesCtrl', CategoriesCtrl);
