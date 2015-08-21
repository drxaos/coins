function AuthI18n($translateProvider) {
    $translateProvider.translations('en', {
        'AUTH_LOGIN_TITLE': "Coins Login",
        'AUTH_LOGIN_NAME': "Name",
        'AUTH_LOGIN_PASSWORD': "Password",
        'AUTH_LOGIN_BUTTON': "Sign in",
        'AUTH_API: wrong-credentials': "Wrong credentials",
        'AUTH_API: server-error': "Server error",
    });

    $translateProvider.translations('ru', {
        'AUTH_LOGIN_TITLE': "Вход в Coins",
        'AUTH_LOGIN_NAME': "Имя",
        'AUTH_LOGIN_PASSWORD': "Пароль",
        'AUTH_LOGIN_BUTTON': "Войти",
        'AUTH_API: wrong-credentials': "Неверный пароль",
        'AUTH_API: server-error': "Ошибка сервера",
    });
}

InitializingModule.config(AuthI18n);
