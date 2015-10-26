package com.github.drxaos.coins.application.database.mysql.migration;

import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.utils.DateUtil;
import com.j256.ormlite.jdbc.JdbcDatabaseConnection;
import liquibase.changelog.ChangeSet;
import liquibase.command.CommandExecutionException;
import liquibase.command.DiffToChangeLogCommand;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.exception.DatabaseException;
import liquibase.serializer.core.xml.XMLChangeLogSerializer;
import liquibase.snapshot.InvalidExampleException;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

public class DiffToolCommand extends ToolCommand {

    @Inject
    MysqlDbMigrationSource source;

    @Inject
    MysqlDbMigrationTarget target;

    @Inject
    DateUtil dateUtil;

    class LiquibaseCommand extends DiffToChangeLogCommand {
        DiffResult diffResult;

        @Override
        protected DiffResult createDiffResult() throws DatabaseException, InvalidExampleException {
            diffResult = super.createDiffResult();
            throw new RuntimeException();
        }

        public DiffResult makeDiffResult() {
            try {
                execute();
            } catch (CommandExecutionException e) {
                // ok
            }
            return diffResult;
        }
    }

    @Override
    public void execute() {

        try {
            Database fromDatabase = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(((JdbcDatabaseConnection) source.getConnectionSource().getReadOnlyConnection()).getInternalConnection()));
            Database toDatabase = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(((JdbcDatabaseConnection) target.getConnectionSource().getReadOnlyConnection()).getInternalConnection()));

            DiffOutputControl diffOutputControl = new DiffOutputControl(false, false, false);
            OutputStream out = System.out;

            LiquibaseCommand command = new LiquibaseCommand();
            command.setTargetDatabase(fromDatabase);
            command.setReferenceDatabase(toDatabase);
            command.setDiffOutputControl(diffOutputControl);
            command.setCompareControl(new CompareControl());
            DiffResult diffResult = command.makeDiffResult();

            DiffToChangeLog diffToChangeLog = new DiffToChangeLog(diffResult, diffOutputControl);
            diffToChangeLog.setChangeSetAuthor("generated");
            diffToChangeLog.setIdRoot(dateUtil.format(dateUtil.now(), "yyyy-MM-dd HH:mm"));
            List<ChangeSet> changeSets = diffToChangeLog.generateChangeSets();
            XMLChangeLogSerializer serializer = new XMLChangeLogSerializer();
            serializer.write(changeSets, out);
            out.flush();

        } catch (DatabaseException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
