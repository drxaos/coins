function MenuI18n($translateProvider) {
    $translateProvider.translations('en', {
        'MENU_GROUP_MAIN': "",
        'MENU_GROUP_SETTINGS': "Settings",
    });

    $translateProvider.translations('ru', {
        'MENU_GROUP_MAIN': "",
        'MENU_GROUP_SETTINGS': "Настройки",
    });
}

InitializingModule.config(MenuI18n);
