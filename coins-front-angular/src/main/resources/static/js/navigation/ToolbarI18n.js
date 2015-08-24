function ToolbarI18n($translateProvider) {
    $translateProvider.translations('en', {
        'TOOLBAR_SEARCH': "Search",
    });

    $translateProvider.translations('ru', {
        'TOOLBAR_SEARCH': "Поиск",
    });
}

InitializingModule.config(ToolbarI18n);
