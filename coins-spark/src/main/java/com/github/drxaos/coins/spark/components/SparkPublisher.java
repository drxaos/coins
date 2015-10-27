package com.github.drxaos.coins.spark.components;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationInitEventListener;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.controller.AbstractRestPublisher;
import com.github.drxaos.coins.controller.RestHandler;
import spark.Spark;

public class SparkPublisher extends AbstractRestPublisher implements ApplicationInitEventListener {

    private Application application;

    @Inject
    JsonTransformer json;

    @Override
    public void publish(Method method, String path, RestHandler handler) {
        switch (method) {
            case GET:
                Spark.get(path, application.getFactory().autowire(new SecureRoute<>(handler)), json);
                return;
            case POST:
                Spark.post(path, application.getFactory().autowire(new SecureRoute<>(handler)), json);
                break;
            case PUT:
                Spark.put(path, application.getFactory().autowire(new SecureRoute<>(handler)), json);
                break;
            case DELETE:
                Spark.delete(path, application.getFactory().autowire(new SecureRoute<>(handler)), json);
                break;
            default:
                throw new IllegalArgumentException("Unknown method: " + method);
        }
    }

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {
        this.application = application;
    }
}
