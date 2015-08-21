function AboutI18n($translateProvider) {
    $translateProvider.translations('en', {
        'ABOUT_MENU_TITLE': 'About',
        'ABOUT_TOOLBAR_TITLE': 'About Coins',
        'ABOUT_TEXT': 'This is About module',
    });

    $translateProvider.translations('ru', {
        'ABOUT_MENU_TITLE': 'О программе',
        'ABOUT_TOOLBAR_TITLE': 'Про Coins',
        'ABOUT_TEXT': 'Это модуль "О программе"',
    });
}

InitializingModule.config(AboutI18n);
