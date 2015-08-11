package com.github.drxaos.coins;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.ApplicationProps;

public class Config extends ApplicationProps {

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {

        props.put("jdbc.url", "jdbc:h2:mem:coins");

    }
}