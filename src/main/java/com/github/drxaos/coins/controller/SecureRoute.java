package com.github.drxaos.coins.controller;

import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.domain.User;
import spark.Request;
import spark.Response;
import spark.Route;

public abstract class SecureRoute<IN, OUT> implements Route {

    @Inject
    protected JsonTransformer json;

    public static class SparkRequest {
        Request request;
        Response response;

        public SparkRequest(Request request, Response response) {
            this.request = request;
            this.response = response;
        }
    }

    ThreadLocal<SparkRequest> threadLocal = new ThreadLocal<>();


    @Override
    public Object handle(Request request, Response response) throws Exception {
        threadLocal.set(new SparkRequest(request, response));
        OUT out = handle();
        threadLocal.set(null);
        return out;
    }

    public abstract OUT handle() throws Exception;

    protected IN input(Class<IN> type) {
        return json.parse(threadLocal.get().request.body(), type);
    }

    protected Request request() {
        return threadLocal.get().request;
    }

    protected Response response() {
        return threadLocal.get().response;
    }

    protected User loggedInUser() {
        return threadLocal.get().request.session().attribute("user");
    }

    protected void auth(User user) {
        threadLocal.get().request.session().attribute("user", user);
    }
}
