package com.github.drxaos.coins.test;

import com.github.drxaos.coins.controller.AbstractRestPublisher;
import com.github.drxaos.coins.controller.RestHandler;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

public class RestPublisherStub extends AbstractRestPublisher {

    @Value
    public static class Handler {
        Method method;
        String path;
        RestHandler handler;
        boolean anonymousAccess;
    }

    Map<String, Handler> handlers = new HashMap<>();

    @Override
    public void publish(Method method, String path, RestHandler handler, boolean anonymousAccess) {
        handlers.put(path, new Handler(method, path, handler, anonymousAccess));
    }

    public Handler getHandler(String path) {
        return handlers.get(path);
    }
}
