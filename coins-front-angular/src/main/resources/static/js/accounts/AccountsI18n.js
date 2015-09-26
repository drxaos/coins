function AccountsI18n($translateProvider) {
    $translateProvider.translations('en', {
        'ACCOUNTS_MENU_TITLE': "Accounts",
        'ACCOUNTS_TOOLBAR_TITLE': "Accounts",
        'ACCOUNTS_DELETE': "Delete",
        'ACCOUNTS_SEARCH': "Search",
        'ACCOUNTS_MENU': "Menu",
        'ACCOUNTS_NEW_SAVE': "Save",
        'ACCOUNTS_NEW_CANCEL': "Cancel",
        'ACCOUNTS_EDIT_NAME': "Name",
        'ACCOUNTS_API: fields-error': "Fields have errors",
        'ACCOUNTS_API: not-found': "Account not found",
        'ACCOUNTS_API: duplicate-entity': "Account '{{name}}' already exists",
        'ACCOUNTS_API: cannot-create': "Cannot save account '{{name}}'",
        'ACCOUNTS_API: cannot-delete': "Cannot delete account '{{name}}'",
        'ACCOUNTS_API: cannot-update': "Cannot save account '{{name}}'",
        'ACCOUNTS_API: entity-deleted': "Account '{{name}}' deleted",
    });

    $translateProvider.translations('ru', {
        'ACCOUNTS_MENU_TITLE': "Счета",
        'ACCOUNTS_TOOLBAR_TITLE': "Счета",
        'ACCOUNTS_DELETE': "Удалить",
        'ACCOUNTS_SEARCH': "Поиск",
        'ACCOUNTS_MENU': "Меню",
        'ACCOUNTS_NEW_SAVE': "Сохранить",
        'ACCOUNTS_NEW_CANCEL': "Отмена",
        'ACCOUNTS_EDIT_NAME': "Название",
        'ACCOUNTS_API: fields-error': "Поля заполнены с ошибками",
        'ACCOUNTS_API: not-found': "Счет не найден",
        'ACCOUNTS_API: duplicate-entity': "Счет '{{name}}' уже существует",
        'ACCOUNTS_API: cannot-create': "Невозможно сохранить счет '{{name}}'",
        'ACCOUNTS_API: cannot-delete': "Невозможно удалить счет '{{name}}'",
        'ACCOUNTS_API: cannot-update': "Невозможно сохранить счет '{{name}}'",
        'ACCOUNTS_API: entity-deleted': "Счет '{{name}}' удален",
    });
}

InitializingModule.config(AccountsI18n);
