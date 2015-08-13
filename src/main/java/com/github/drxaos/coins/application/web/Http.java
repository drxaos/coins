package com.github.drxaos.coins.application.web;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationInit;
import com.github.drxaos.coins.application.events.ApplicationStop;
import com.github.drxaos.coins.application.factory.Component;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import spark.Spark;
import spark.SparkBase;
import spark.webserver.SparkServer;

import java.lang.reflect.Field;

@Slf4j
@Component
public class Http implements ApplicationInit, ApplicationStop {

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {

        int maxThreads = 1 + Runtime.getRuntime().availableProcessors() * 2; // https://github.com/perwendel/spark/issues/306
        int minThreads = 2;
        int timeOutMillis = 5000;
        Spark.threadPool(maxThreads, minThreads, timeOutMillis);

        Spark.port(4567);

        Spark.staticFileLocation("static");

        // Hack Spark server to support multiple folders with static resources
        new Thread(() -> {
            try {
                Spark.awaitInitialization();
                Field sparkServerField = SparkBase.class.getDeclaredField("server");
                sparkServerField.setAccessible(true);
                SparkServer sparkServer = (SparkServer) sparkServerField.get(null);
                Field serverField = SparkServer.class.getDeclaredField("server");
                serverField.setAccessible(true);
                Server server = (Server) serverField.get(sparkServer);
                ResourceCollection r = new ResourceCollection(Resource.newClassPathResource("static"), Resource.newClassPathResource("/META-INF/resources/"));
                Handler[] handlers = ((HandlerList) server.getHandler()).getHandlers();
                for (Handler handler : handlers) {
                    if (handler instanceof ResourceHandler) {
                        ((ResourceHandler) handler).setBaseResource(r);
                    }
                }
            } catch (Exception e) {
                log.error("Spark init error", e);
            }
        }).start();
    }

    @Override
    public void onApplicationStop(Application application) throws ApplicationInitializationException {
        Spark.stop();
    }
}
