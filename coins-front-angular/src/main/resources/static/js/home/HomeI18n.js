function HomeI18n($translateProvider) {
    $translateProvider.translations('en', {
        'HOME_MENU_TITLE': "Browse",
        'HOME_TOOLBAR_TITLE': "Browse",
    });

    $translateProvider.translations('ru', {
        'HOME_MENU_TITLE': "Обзор",
        'HOME_TOOLBAR_TITLE': "Обзор",
    });
}

InitializingModule.config(HomeI18n);
