package com.github.drxaos.coins.application.database.mysql.migration;

import com.github.drxaos.coins.CoinsCoreModule;
import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.utils.DateUtil;

public class MigrationTool {

    static String[] params;

    public static void main(String[] args) throws ApplicationInitializationException {
        params = args;

        Application application = new Application() {
            @Override
            public void init() throws ApplicationInitializationException {
                addClasses(CoinsCoreModule.TYPES);
                addObjects(
                        MigrationConfig.class,
                        DateUtil.class
                );

                if (args.length < 1 || args[0].equals("help")) {
                    addObjects(HelpToolCommand.class);
                } else {
                    switch (args[0]) {
                        case "diff":
                            addObjects(DiffToolCommand.class, MysqlDbMigrationSource.class, MysqlDbMigrationTarget.class);
                            break;
                        case "migrate":
                            addObjects(MigrateToolCommand.class, MysqlDbMigrationSource.class);
                            break;
                        case "release":
                            addObjects(ReleaseToolCommand.class, MysqlDbMigrationSource.class);
                            break;
                    }
                }
            }
        };

        application.start();
    }

}
