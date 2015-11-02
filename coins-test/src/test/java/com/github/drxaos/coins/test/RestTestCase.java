package com.github.drxaos.coins.test;

import com.github.drxaos.coins.CoinsCoreModule;
import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.config.ApplicationProps;
import com.github.drxaos.coins.application.database.h2.CoinsDbH2Module;
import com.github.drxaos.coins.application.database.h2.H2Db;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.application.test.Fixtures;
import com.github.drxaos.coins.mock.DateUtilMock;
import com.github.drxaos.coins.spark.CoinsSparkModule;
import com.github.drxaos.coins.spark.config.Http;
import com.github.drxaos.coins.utils.DateUtil;
import com.google.common.collect.FluentIterable;
import com.jayway.jsonpath.JsonPath;
import lombok.Value;
import lombok.experimental.Accessors;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.experimental.categories.Category;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import spark.Spark;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

@Category(RestCategory.class)
@RunWith(RestRunner.class)
abstract public class RestTestCase extends AbstractTestCase {

    @Rule
    public TestName name = new TestName();

    public static class Config extends ApplicationProps {

        @Override
        public void onApplicationInit(Application application) throws ApplicationInitializationException {
            String forkNumber = System.getProperty("forkNumber");
            if (forkNumber == null || forkNumber.isEmpty() || !forkNumber.matches("[0-9]+]")) {
                forkNumber = "1";
            }
            props.put("server.port", "4568");
            props.put("server.host", "127.0.0." + forkNumber);
        }
    }

    Application application;

    protected String baseUrl;

    @Inject
    ApplicationProps props;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        H2DbHelper.init();
        H2DbHelper.testName = this.getClass().getName() + "." + name.getMethodName();
        H2DbHelper.prepareState();

        application = new Application() {
            @Override
            public void init() {
                addClasses(CoinsCoreModule.TYPES);
                addObjects(FluentIterable.from(CoinsCoreModule.COMPONENTS).filter((c) -> c != DateUtil.class).toList());
                addObjects(DateUtilMock.class);
                addObjects(CoinsSparkModule.COMPONENTS);
                addObjects(Config.class);
                addObjects(FluentIterable.from(CoinsDbH2Module.COMPONENTS).filter((c) -> c != H2Db.class).toList());
                addObjects(H2DbHelper.class);
                addObjects(Fixtures.class);
            }
        };
        application.start();
        Spark.awaitInitialization();

        Http http = application.getFactory().getObject(Http.class);
        baseUrl = "http://" + http.host + ":" + http.port + "";
        application.getFactory().autowire(this);

        int count = 0;
        boolean ok = false;
        while (!ok && count < 10) {
            try {
                get("/");
                ok = true;
            } catch (SocketException e) {
                Thread.sleep(100);
            }
            count++;
        }
    }

    @After
    public void tearDown() throws Exception {
        application.stop();
        H2DbHelper.removeState();
        super.tearDown();
    }

    @Value
    @Accessors(fluent = true, chain = true)
    public static class Response {
        int code;
        String content;

        public <T> T query(String path) {
            return JsonPath.read(content, path);
        }
    }

    @FunctionalInterface
    interface Method {
        Request make(String url);
    }

    private Map<String, Method> methods = new HashMap<String, Method>() {{
        put("get", (url) -> Request.Get(baseUrl + url));
        put("post", (url) -> Request.Post(baseUrl + url));
        put("put", (url) -> Request.Put(baseUrl + url));
        put("delete", (url) -> Request.Delete(baseUrl + url));
    }};

    String currentSession = "default";
    Map<String, BasicCookieStore> sessions = new HashMap<String, BasicCookieStore>() {{
        put(currentSession, new BasicCookieStore());
    }};

    protected String getCurrentSession() {
        return currentSession;
    }

    protected void setCurrentSession(String currentSession) {
        this.currentSession = currentSession;
        if (!sessions.containsKey(currentSession)) {
            sessions.put(currentSession, new BasicCookieStore());
        }
    }

    protected void setSessionId(String sessionId) {
        BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", sessionId);
        cookie.setPath("/");
        cookie.setAttribute("Path", "/");
        cookie.setDomain(props.getString("server.host", "127.0.0.1"));
        sessions.get(currentSession).addCookie(cookie);
    }

    protected Response exec(String method, String url, String data) throws IOException {
        Request request = methods.get(method).make(url);
        if (data != null && !data.isEmpty()) {
            request = request.bodyString(data, ContentType.APPLICATION_JSON);
        }

        CookieStore cookieStore = sessions.get(currentSession);
        Executor executor = Executor.newInstance();

        HttpResponse httpResponse = executor.use(cookieStore)
                .execute(request).returnResponse();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        httpResponse.getEntity().writeTo(out);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        return new Response(statusCode, new String(out.toByteArray()));
    }

    protected Response get(String url) throws IOException {
        return exec("get", url, null);
    }

    protected Response post(String url, String data) throws IOException {
        return exec("post", url, data);
    }

    protected Response put(String url, String data) throws IOException {
        return exec("put", url, data);
    }

    protected Response delete(String url, String data) throws IOException {
        return exec("delete", url, data);
    }
}
