function AccountEntity($resource) {
    return $resource('/api/v1/accounts/:id', {id: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
}

InitializingModule.factory('AccountEntity', AccountEntity);
