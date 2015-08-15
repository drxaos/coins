function CategoriesCollection($resource) {
    return $resource('/api/v1/categories/:id');
}

InitializingModule.factory('CategoriesCollection', CategoriesCollection);
