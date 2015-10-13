package com.github.drxaos.coins.application.database.h2.migration;

import com.github.drxaos.coins.CoinsCoreModule;
import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.config.ApplicationProps;
import com.github.drxaos.coins.utils.DateUtil;

public class MigrationTool {

    public static class Config extends ApplicationProps {

        @Override
        public void onApplicationInit(Application application) throws ApplicationInitializationException {
            props.put("jdbc.url", "jdbc:h2:./coins;database_To_Upper=false");
            props.put("ref.jdbc.url", "jdbc:h2:mem:tmp;DB_CLOSE_DELAY=-1");
        }
    }

    public static void main(String[] args) throws ApplicationInitializationException {
        Application application = new Application() {
            @Override
            public void init() {
                addClasses(CoinsCoreModule.TYPES);
                addObjects(
                        Config.class,
                        H2DbMigrationSource.class,
                        H2DbMigrationTarget.class,
                        DateUtil.class
                );
            }
        };

        application.start();
    }

}
