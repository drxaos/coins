package com.github.drxaos.coins.application.database.h2.migration;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.database.h2.H2Db;
import com.github.drxaos.coins.application.events.ApplicationStartEventListener;
import com.github.drxaos.coins.application.factory.Component;
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
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class H2DbMigrationTarget extends H2Db implements ApplicationStartEventListener {

    @Inject
    H2DbMigrationSource source;

    @Inject
    DateUtil dateUtil;

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {
        log.info("opening reference database: " + getJdbcUrl());
        setCreateSchema(true);
        super.onApplicationInit(application);
    }

    @Override
    public String getJdbcUrl() {
        return props.getString("ref.jdbc.url", "jdbc:h2:mem:test");
    }

    class Command extends DiffToChangeLogCommand {
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
    public void onApplicationStart(Application application) throws ApplicationInitializationException {
        log.info("ready to migrate");

        try {
            Database fromDatabase = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(((JdbcDatabaseConnection) source.getConnectionSource().getReadOnlyConnection()).getInternalConnection()));
            Database toDatabase = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(((JdbcDatabaseConnection) this.getConnectionSource().getReadOnlyConnection()).getInternalConnection()));

            DiffOutputControl diffOutputControl = new DiffOutputControl(false, false, false);
            OutputStream out = System.out;

            Command command = new Command();
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
