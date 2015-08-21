function CategoriesI18n($translateProvider) {
    $translateProvider.translations('en', {
        'CATEGORIES_MENU_TITLE': "Categories",
        'CATEGORIES_TOOLBAR_TITLE': "Categories",
        'CATEGORIES_EXPENSE': "Expense",
        'CATEGORIES_INCOME': "Income",
        'CATEGORIES_DELETE': "Delete",
        'CATEGORIES_SEARCH': "Search",
        'CATEGORIES_MENU': "Menu",
        'CATEGORIES_NEW_SAVE': "Save",
        'CATEGORIES_NEW_CANCEL': "Cancel",
        'CATEGORIES_EDIT_NAME': "Name",
        'CATEGORIES_API: fields-error': "Fields have errors",
        'CATEGORIES_API: not-found': "Category not found",
        'CATEGORIES_API: duplicate-entity': "Category '{{name}}' already exists",
        'CATEGORIES_API: cannot-create': "Cannot save category '{{name}}'",
        'CATEGORIES_API: cannot-delete': "Cannot delete category '{{name}}'",
        'CATEGORIES_API: cannot-update': "Cannot save category '{{name}}'",
        'CATEGORIES_API: entity-deleted': "Category '{{name}}' deleted",
    });

    $translateProvider.translations('ru', {
        'CATEGORIES_MENU_TITLE': "Категории",
        'CATEGORIES_TOOLBAR_TITLE': "Категории",
        'CATEGORIES_EXPENSE': "Расходы",
        'CATEGORIES_INCOME': "Доходы",
        'CATEGORIES_DELETE': "Удалить",
        'CATEGORIES_SEARCH': "Поиск",
        'CATEGORIES_MENU': "Меню",
        'CATEGORIES_NEW_SAVE': "Сохранить",
        'CATEGORIES_NEW_CANCEL': "Отмена",
        'CATEGORIES_EDIT_NAME': "Название",
        'CATEGORIES_API: fields-error': "Поля заполнены с ошибками",
        'CATEGORIES_API: not-found': "Категория не найдена",
        'CATEGORIES_API: duplicate-entity': "Категория '{{name}}' уже существует",
        'CATEGORIES_API: cannot-create': "Невозможно сохранить категорию '{{name}}'",
        'CATEGORIES_API: cannot-delete': "Невозможно удалить категорию '{{name}}'",
        'CATEGORIES_API: cannot-update': "Невозможно сохранить категорию '{{name}}'",
        'CATEGORIES_API: entity-deleted': "Категория '{{name}}' удалена",
    });
}

InitializingModule.config(CategoriesI18n);
