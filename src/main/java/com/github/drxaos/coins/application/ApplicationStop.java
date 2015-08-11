package com.github.drxaos.coins.application;

public interface ApplicationStop {
    void onApplicationStop(Application application) throws ApplicationInitializationException;
}
