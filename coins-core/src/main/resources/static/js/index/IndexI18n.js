function IndexI18n($translateProvider) {
    $translateProvider.translations('en', {
        'INDEX_MENU_LOGOUT': "Logout"
    });

    $translateProvider.translations('ru', {
        'INDEX_MENU_LOGOUT': "Выход"
    });

    $translateProvider.preferredLanguage('en');
}

InitializingModule.config(IndexI18n);
