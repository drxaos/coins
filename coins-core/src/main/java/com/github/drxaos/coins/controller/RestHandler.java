package com.github.drxaos.coins.controller;

public abstract class RestHandler<IN, OUT> {

    protected AbstractTransport<IN, OUT> transport;

    public OUT handle(AbstractTransport<IN, OUT> transport) throws Exception {
        this.transport = transport;
        return handle();
    }

    abstract public OUT handle() throws Exception;

}
