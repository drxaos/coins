function CategoriesI18n($translateProvider) {
    $translateProvider.translations('en', {
        'CATEGORIES_MENU_TITLE': "Categories",
        'CATEGORIES_TOOLBAR_TITLE': "Categories",
        'CATEGORIES_EXPENSE': "Expense",
        'CATEGORIES_INCOME': "Income",
        'CATEGORIES_DELETE': "Delete",
        'CATEGORIES_SEARCH': "Search",
        'CATEGORIES_MENU': "Menu",
    });

    $translateProvider.translations('ru', {
        'CATEGORIES_MENU_TITLE': "Категории",
        'CATEGORIES_TOOLBAR_TITLE': "Категории",
        'CATEGORIES_EXPENSE': "Расходы",
        'CATEGORIES_INCOME': "Доходы",
        'CATEGORIES_DELETE': "Удалить",
        'CATEGORIES_SEARCH': "Поиск",
        'CATEGORIES_MENU': "Меню",
    });
}

InitializingModule.config(CategoriesI18n);
