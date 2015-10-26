package com.github.drxaos.coins.application.database.mysql.migration;

import com.github.drxaos.coins.application.factory.Inject;
import com.j256.ormlite.jdbc.JdbcDatabaseConnection;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.integration.commandline.CommandLineResourceAccessor;
import liquibase.resource.CompositeResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;

import java.sql.SQLException;

public class MigrateToolCommand extends ToolCommand {

    @Inject
    MysqlDbMigrationSource source;

    @Override
    public void execute() {

        try {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(((JdbcDatabaseConnection) source.getConnectionSource().getReadWriteConnection()).getInternalConnection()));
            FileSystemResourceAccessor fsOpener = new FileSystemResourceAccessor();
            CommandLineResourceAccessor clOpener = new CommandLineResourceAccessor(this.getClass().getClassLoader());
            CompositeResourceAccessor fileOpener = new CompositeResourceAccessor(fsOpener, clOpener);

            Liquibase liquibase = new Liquibase("migration/changelog.xml", fileOpener, database);
            liquibase.update(new Contexts(), new LabelExpression("!release"));

        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
        }
    }
}
