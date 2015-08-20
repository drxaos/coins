function CategoriesCtrl(AuthService, CategoryEntity, $rootScope, $mdToast, $translate) {
    var model = this;

    AuthService.checkLoggedIn().then(function () {
        return CategoryEntity.query().$promise;
    }).then(function (entries) {

        // list

        model.list = entries;
        model.editing = null;

        function showMessage(data) {
            var msg = null;
            var obj = null;
            var error = false;
            if (data.success) {
                msg = data.success;
                obj = data.data;
            } else if (data.data && data.data.success) {
                msg = data.data.success;
                obj = data.data.data;
            } else if (data.error) {
                msg = data.error;
                obj = data.data;
                error = true;
            } else if (data.data && data.data.error) {
                msg = data.data.error;
                obj = data.data.data;
                error = true;
            }
            if (msg != null) {
                $translate('CATEGORIES_API: ' + msg, obj).then(function (text) {
                    $mdToast.show({
                        template: "<md-toast>" + text + "</md-toast>"
                    });
                });
            }
            if (error) {
                $(".categories-new-item").find("input").focus().select();
            }
        }

        function cleanList() {
            model.list = model.list.filter(function (el) {
                return el.id !== undefined;
            });
        }

        model.updateEntity = function (item) {
            if (item.newEntity) {
                return;
            }
            item.$update(function (res) {
                item.editEntity = null;
                showMessage(res);
            }, showMessage);
        };

        model.editEntity = function (item) {
            if (model.editing != null) {
                model.cancelEntity(model.editing);
            }
            item.editEntity = true;
            model.editing = item;
        };

        model.deleteEntity = function (item) {
            var name = item.name;
            item.$delete(function (res) {
                res.data.name = name;
                cleanList();
                showMessage(res);
            }, showMessage);
        };

        model.addEntity = function () {
            if (model.editing != null) {
                model.cancelEntity(model.editing);
            }

            for (e in model.list) {
                if (model.list[e].newEntity) {
                    $(".categories-new-item").find("input").focus().select();
                    return;
                }
            }

            var item = new CategoryEntity({
                name: "",
                expense: false,
                income: false,
                newEntity: true
            });

            model.list.push(item);
            model.editing = item;

            $(".app-view").animate({scrollTop: $(".app-view")[0].scrollHeight - 250}, 300);
        };

        model.saveEntity = function (item) {
            if (item.newEntity) {
                CategoryEntity.save(item, function (saved) {
                    model.list[model.list.indexOf(item)] = saved;
                    model.addEntity();
                }, showMessage);
            } else if (item.editEntity) {
                model.updateEntity(item);
            }
        };

        model.cancelEntity = function (item) {
            if (item.newEntity) {
                cleanList();
            } else if (item.editEntity) {
                item.editEntity = false;
                CategoryEntity.get({id: item.id}, function (loaded) {
                    model.list[model.list.indexOf(item)] = loaded;
                }, showMessage);
            }
        };

        // menu

        function search() {
            if (model.editing != null) {
                model.cancelEntity(model.editing);
            }
            $rootScope.appSearch = !$rootScope.appSearch;
        }

        model.search = {name: ""};

        $rootScope.toolbarTools = [
            {name: "CATEGORIES_SEARCH", icon: "ion-search fa-lg", click: search},
            //{name: "CATEGORIES_MENU", icon: "ion-android-more-vertical fa-lg", click: menu},
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
