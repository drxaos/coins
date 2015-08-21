package com.github.drxaos.coins.application.events;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;

public interface ApplicationStart {
    void onApplicationStart(Application application) throws ApplicationInitializationException;
}
