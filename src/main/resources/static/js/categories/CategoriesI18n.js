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
        'CATEGORIES_ERROR_duplicate-entity': "Category already exists",
        'CATEGORIES_ERROR_cannot-delete': "Cannot delete category",
        'CATEGORIES_SUCCESS_entity-deleted': "Category deleted",
        'CATEGORIES_SUCCESS_entity-saved': "Category saved",
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
        'CATEGORIES_ERROR_duplicate-entity': "Такая категория уже существует",
        'CATEGORIES_ERROR_cannot-delete': "Невозможно удалить эту категорию",
        'CATEGORIES_SUCCESS_entity-deleted': "Категория удалена",
        'CATEGORIES_SUCCESS_entity-saved': "Категория сохранена",
    });
}

InitializingModule.config(CategoriesI18n);
