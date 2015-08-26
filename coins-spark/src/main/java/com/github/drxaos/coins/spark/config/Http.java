package com.github.drxaos.coins.spark.config;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationInit;
import com.github.drxaos.coins.application.events.ApplicationStop;
import com.github.drxaos.coins.application.factory.Component;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.spark.sessions.DbSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import spark.Spark;
import spark.SparkBase;
import spark.webserver.SparkServer;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Http implements ApplicationInit, ApplicationStop {

    @Inject
    DbSessionManager dbSessionManager;

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {

        int maxThreads = 1 + Runtime.getRuntime().availableProcessors() * 2; // https://github.com/perwendel/spark/issues/306
        int minThreads = 2;
        int timeOutMillis = 5000;
        Spark.threadPool(maxThreads, minThreads, timeOutMillis);

        Spark.port(4567);

        Spark.staticFileLocation("static");

        // Hack Spark server to support multiple folders with static resources
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Spark.awaitInitialization();
                    Field sparkServerField = SparkBase.class.getDeclaredField("server");
                    sparkServerField.setAccessible(true);
                    SparkServer sparkServer = (SparkServer) sparkServerField.get(null);
                    Field serverField = SparkServer.class.getDeclaredField("server");
                    serverField.setAccessible(true);
                    Server server = (Server) serverField.get(sparkServer);
                    server.stop();
                    Field handlerField = SparkServer.class.getDeclaredField("handler");
                    handlerField.setAccessible(true);
                    SessionHandler sessionHandler = (SessionHandler) handlerField.get(sparkServer);

                    List<Resource> resources = new ArrayList<Resource>();
                    resources.add(Resource.newClassPathResource("static"));

                    ClassLoader cl = ClassLoader.getSystemClassLoader();
                    URL[] urls = ((URLClassLoader) cl).getURLs();
                    for (URL url : urls) {
                        try {
                            URL test = new URL("jar:" + url.toExternalForm() + "!/META-INF/resources/");
                            test.openConnection().connect();
                            resources.add(Resource.newResource(test));
                        } catch (Exception e) {
                            // not found
                        }
                    }
                    ResourceCollection r = new ResourceCollection(resources.toArray(new Resource[resources.size()]));

                    Handler[] handlers = ((HandlerList) server.getHandler()).getHandlers();
                    for (Handler handler : handlers) {
                        if (handler instanceof ResourceHandler) {
                            ((ResourceHandler) handler).setBaseResource(r);
                        }
                    }

                    sessionHandler.setSessionManager(dbSessionManager);

                    server.start();
                } catch (Exception e) {
                    log.error("Spark init error", e);
                }
            }
        }).start();
    }

    @Override
    public void onApplicationStop(Application application) throws ApplicationInitializationException {
        Spark.stop();
    }
}
