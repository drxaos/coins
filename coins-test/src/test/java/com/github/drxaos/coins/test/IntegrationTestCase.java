package com.github.drxaos.coins.test;

import com.github.drxaos.coins.CoinsCoreModule;
import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.config.ApplicationProps;
import com.github.drxaos.coins.application.database.h2.CoinsDbH2Module;
import com.github.drxaos.coins.application.database.h2.H2Db;
import com.google.common.collect.FluentIterable;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.experimental.categories.Category;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicReference;

@Category(IntegrationCategory.class)
@RunWith(IntegrationRunner.class)
abstract public class IntegrationTestCase extends AbstractTestCase {

    @Rule
    public TestName name = new TestName();

    public static class Config extends ApplicationProps {
        @Override
        public void onApplicationInit(Application application) throws ApplicationInitializationException {
        }
    }

    static final AtomicReference<Boolean> dbInitialized = new AtomicReference<>(false);

    @Before
    public void setUp() throws Exception {
        super.setUp();

        synchronized (dbInitialized) {
            if (!dbInitialized.get()) {
                H2DbHelper.clearState(true);

                H2DbHelper.testName = "test";
                Application application = new Application() {
                    @Override
                    public void init() {
                        addClasses(CoinsCoreModule.TYPES);
                        addObjects(Config.class);
                        addObjects(FluentIterable.from(CoinsDbH2Module.COMPONENTS).filter((c) -> c != H2Db.class).toList());
                        addObjects(H2DbHelper.class);
                    }
                };
                application.start();
                application.getFactory().getObject(H2DbHelper.class).executeMigration();
                application.stop();

                dbInitialized.set(true);
            }
        }

        H2DbHelper.testName = this.getClass().getName() + "." + name.getMethodName();
        H2DbHelper.prepareState();

        Application application = new Application() {
            @Override
            public void init() {
                addClasses(CoinsCoreModule.TYPES);
                addObjects(CoinsCoreModule.COMPONENTS);
                addObjects(RestPublisherStub.class);
                addObjects(Config.class);
                addObjects(FluentIterable.from(CoinsDbH2Module.COMPONENTS).filter((c) -> c != H2Db.class).toList());
                addObjects(H2DbHelper.class);
            }
        };
        application.start();

        application.getFactory().autowire(this);
    }

    @After
    public void tearDown() throws Exception {
        H2DbHelper.removeState();
    }
}
