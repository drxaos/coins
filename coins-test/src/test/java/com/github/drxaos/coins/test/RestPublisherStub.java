package com.github.drxaos.coins.test;

import com.github.drxaos.coins.controller.AbstractRestPublisher;
import com.github.drxaos.coins.controller.RestHandler;

public class RestPublisherStub extends AbstractRestPublisher {
    @Override
    public void publish(Method method, String path, RestHandler handler, boolean anonymousAccess) {

    }
}
