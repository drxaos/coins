package com.github.drxaos.coins.spark.components;

import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.controller.AbstractTransport;
import com.github.drxaos.coins.controller.RestHandler;
import com.github.drxaos.coins.controller.auth.AccessDeniedResponse;
import com.github.drxaos.coins.domain.User;
import com.github.drxaos.coins.spark.sessions.DbSessionManager;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;

@Slf4j
public class SecureRoute<IN, OUT> implements Route, AbstractTransport<IN, OUT> {

    @Inject
    protected JsonTransformer json;

    @Inject
    DbSessionManager sessionManager;

    @Inject
    Db db;

    protected RestHandler<IN, OUT> handler;

    protected boolean anonymousAccess = false;

    public static class SparkRequest {
        Request request;
        Response response;

        public SparkRequest(Request request, Response response) {
            this.request = request;
            this.response = response;
        }
    }

    public SecureRoute(RestHandler<IN, OUT> handler, boolean anonymousAccess) {
        this.handler = handler;
        this.anonymousAccess = anonymousAccess;
    }

    ThreadLocal<SparkRequest> threadLocal = new ThreadLocal<>();

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            threadLocal.set(new SparkRequest(request, response));

            if (!anonymousAccess && loggedInUser() == null) {
                return new AccessDeniedResponse();
            }

            OUT out = handler.handle(this);
            return out;

        } finally {
            threadLocal.set(null);
        }
    }

    public IN input(Class<IN> type) {
        return json.parse(threadLocal.get().request.body(), type);
    }

    public Request request() {
        return threadLocal.get().request;
    }

    public Response response() {
        return threadLocal.get().response;
    }

    public User loggedInUser() {
        User user = threadLocal.get().request.session().attribute("user");
        if (user == null) {
            Long userId = threadLocal.get().request.session().attribute("__userId");
            if (userId != null) {
                try {
                    user = db.getDao(User.class).queryForId(userId);
                    auth(user);
                } catch (SQLException | TypedSqlException e) {
                    // no user
                }
            }
        }
        return user;
    }

    public void auth(User user) {
        threadLocal.get().request.session().attribute("user", user);
        threadLocal.get().request.session().attribute("__userId", user != null ? user.id() : null);
    }

    public void dropActiveSessions(boolean keepCurrent) {
        User user = loggedInUser();
        String sessionId = keepCurrent ? threadLocal.get().request.session().id() : "";
        try {
            sessionManager.invalidateSessions(user.id(), sessionId);
        } catch (Exception e) {
            log.error("cannot invalidate sessions", e);
        }
    }

    @Override
    public String params(String name) {
        return threadLocal.get().request.params(name);
    }

    @Override
    public void status(int httpCode) {
        threadLocal.get().response.status(httpCode);
    }
}
