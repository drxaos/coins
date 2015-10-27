package com.github.drxaos.coins.application.database.mysql.migration;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.database.mysql.MysqlDb;
import com.github.drxaos.coins.application.events.ApplicationStopEventListener;
import com.github.drxaos.coins.application.factory.Component;
import com.github.drxaos.coins.application.factory.Inject;
import com.j256.ormlite.support.DatabaseConnection;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
@Component
public class MysqlDbMigrationTarget extends MysqlDb implements ApplicationStopEventListener {

    @Inject
    MysqlDbMigrationSource source;

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {
        setCreateSchema(true);

        controlRefDb(false);
        controlRefDb(true);

        log.info("opening reference database: " + getJdbcUrl());
        super.onApplicationInit(application);
    }

    @Override
    public String getJdbcUrl() {
        return props.getString("ref.jdbc.url", "jdbc:mysql://localhost/coins_tmp?user=root&password=root");
    }

    @Override
    public void onApplicationStop(Application application) throws ApplicationInitializationException {
        super.onApplicationStop(application);
        controlRefDb(false);
    }

    private void controlRefDb(boolean on) throws ApplicationInitializationException {
        log.info((on ? "creating" : "deleting") + " reference database: " + getJdbcUrl());
        DatabaseConnection connection = null;
        String stmt = (on ? "create" : "drop") + " database " + (on ? "" : "if exists ") + props.getString("ref.jdbc.dbName", "tmp") + ";";
        try {
            connection = source.getConnectionSource().getReadWriteConnection();
            connection.executeStatement(stmt, 0);
        } catch (SQLException e) {
            throw new ApplicationInitializationException("Could not " + (on ? "create" : "drop") + " tmp db (" + stmt + ")", e);
        } finally {
            if (connection != null) {
                try {
                    source.getConnectionSource().releaseConnection(connection);
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }
}
