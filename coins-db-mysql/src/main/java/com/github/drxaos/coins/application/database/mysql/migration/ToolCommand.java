package com.github.drxaos.coins.application.database.mysql.migration;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationStartEventListener;
import com.github.drxaos.coins.application.factory.Component;

@Component
public abstract class ToolCommand implements ApplicationStartEventListener {

    public abstract void execute();

    @Override
    public void onApplicationStart(Application application) throws ApplicationInitializationException {
        execute();
        application.stop();
    }
}
