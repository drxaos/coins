package com.github.drxaos.coins.controller;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationStartEventListener;
import com.github.drxaos.coins.application.factory.Inject;
import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;

@Slf4j
public class AbstractRestController implements ApplicationStartEventListener {

    @Inject
    AbstractRestPublisher publisher;

    @Override
    public void onApplicationStart(Application application) throws ApplicationInitializationException {

        Class cls = this.getClass();
        Class c = cls;
        while (c != null && c != Object.class) {
            String context = "/api/v1/" + this.getClass().getSimpleName();

            PublishingContext publishingContext = (PublishingContext) c.getAnnotation(PublishingContext.class);
            if (publishingContext != null) {
                context = publishingContext.value();
            }

            for (Field field : c.getDeclaredFields()) {
                {
                    Publish publish = (Publish) Iterables.tryFind(
                            Arrays.asList(field.getDeclaredAnnotations()),
                            (a) -> (a instanceof Publish)).orNull();

                    if (publish != null) {
                        try {
                            publisher.publish(publish.method(), context + publish.path(), (RestHandler) field.get(this), publish.anonymousAccess());
                        } catch (IllegalAccessException e) {
                            log.error("cannot publish action", e);
                        }

                    }
                }
            }
            c = c.getSuperclass();
        }
    }

}
