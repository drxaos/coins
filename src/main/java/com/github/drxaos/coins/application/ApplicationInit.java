package com.github.drxaos.coins.application;

public interface ApplicationInit {
    void onApplicationInit(Application application) throws ApplicationInitializationException;
}
