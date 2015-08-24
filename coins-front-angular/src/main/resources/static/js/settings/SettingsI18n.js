function SettingsI18n($translateProvider) {
    $translateProvider.translations('en', {
        'SETTINGS_LANGUAGE': "Language",
        'SETTINGS_MENU_TITLE': "Settings",
        'SETTINGS_TOOLBAR_TITLE': "Settings",
    });

    $translateProvider.translations('ru', {
        'SETTINGS_LANGUAGE': "Язык",
        'SETTINGS_MENU_TITLE': "Настройки",
        'SETTINGS_TOOLBAR_TITLE': "Настройки",
    });
}

InitializingModule.config(SettingsI18n);
