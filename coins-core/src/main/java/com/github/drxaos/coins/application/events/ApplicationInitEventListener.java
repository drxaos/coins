package com.github.drxaos.coins.application.events;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;

public interface ApplicationInitEventListener {
    void onApplicationInit(Application application) throws ApplicationInitializationException;
}
