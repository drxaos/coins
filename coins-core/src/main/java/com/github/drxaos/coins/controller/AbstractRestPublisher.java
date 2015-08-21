package com.github.drxaos.coins.controller;

import com.github.drxaos.coins.application.factory.Component;

@Component
abstract public class AbstractRestPublisher {

    public enum Method {
        GET, POST, PUT, DELETE;
    }

    abstract public void publish(Method method, String path, RestHandler handler);
}
