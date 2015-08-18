function CategoryEntity($resource) {
    return $resource('/api/v1/categories/:id', {id: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
}

InitializingModule.factory('CategoryEntity', CategoryEntity);
