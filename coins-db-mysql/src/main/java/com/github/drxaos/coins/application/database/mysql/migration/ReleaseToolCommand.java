package com.github.drxaos.coins.application.database.mysql.migration;

import com.github.drxaos.coins.application.factory.Inject;
import com.j256.ormlite.jdbc.JdbcDatabaseConnection;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.RuntimeEnvironment;
import liquibase.changelog.ChangeLogIterator;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.changelog.filter.ChangeSetFilter;
import liquibase.changelog.filter.ChangeSetFilterResult;
import liquibase.changelog.visitor.ChangeSetVisitor;
import liquibase.changelog.visitor.RollbackVisitor;
import liquibase.changelog.visitor.UpdateVisitor;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.executor.ExecutorService;
import liquibase.executor.LoggingExecutor;
import liquibase.integration.commandline.CommandLineResourceAccessor;
import liquibase.resource.CompositeResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class ReleaseToolCommand extends ToolCommand {
    @Inject
    MysqlDbMigrationSource source;

    @Override
    public void execute() {

        try {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(((JdbcDatabaseConnection) source.getConnectionSource().getReadOnlyConnection()).getInternalConnection()));

            FileSystemResourceAccessor fsOpener = new FileSystemResourceAccessor();
            CommandLineResourceAccessor clOpener = new CommandLineResourceAccessor(this.getClass().getClassLoader());
            CompositeResourceAccessor fileOpener = new CompositeResourceAccessor(fsOpener, clOpener);

            Liquibase liquibase = new Liquibase("migration/changelog.xml", fileOpener, database);
            DatabaseChangeLog changeLog = liquibase.getDatabaseChangeLog();
            ChangeLogIterator changeLogIterator = new ChangeLogIterator(changeLog,
                    new ChangeSetFilter() {
                        @Override
                        public ChangeSetFilterResult accepts(ChangeSet changeSet) {
                            return new ChangeSetFilterResult(true, "", this.getClass());
                        }
                    });

            AtomicReference<String> prevRelease = new AtomicReference<>();
            AtomicReference<String> lastRelease = new AtomicReference<>();

            if (MigrationTool.params.length >= 3) {
                prevRelease.set(MigrationTool.params[1]);
                lastRelease.set(MigrationTool.params[2]);
            } else {
                changeLogIterator.run(new ChangeSetVisitor() {
                    @Override
                    public Direction getDirection() {
                        return Direction.FORWARD;
                    }

                    @Override
                    public void visit(ChangeSet changeSet, DatabaseChangeLog databaseChangeLog, Database database, Set<ChangeSetFilterResult> filterResults) throws LiquibaseException {
                        if (changeSet.getLabels().getLabels().contains("release")) {
                            prevRelease.set(lastRelease.get());
                            lastRelease.set(changeSet.getComments());
                        }
                    }
                }, new RuntimeEnvironment(database, new Contexts(), new LabelExpression()));
            }

            System.out.println("" + prevRelease.get() + " -> " + lastRelease.get());

            AtomicReference<Boolean> active = new AtomicReference<>();
            active.set(prevRelease.get() == null);


            File dir = new File("coins-db-mysql/src/main/resources/migration/releases/" + lastRelease.get());
            dir.mkdirs();

            {
                FileWriter output = new FileWriter(new File(dir, "" + lastRelease.get() + ".sql"));

                LoggingExecutor loggingExecutor = new LoggingExecutor(ExecutorService.getInstance().getExecutor(database), output, database);
                ExecutorService.getInstance().setExecutor(database, loggingExecutor);

                changeLogIterator.run(new UpdateVisitor(database, null) {
                    @Override
                    public void visit(ChangeSet changeSet, DatabaseChangeLog databaseChangeLog, Database database, Set<ChangeSetFilterResult> filterResults) throws LiquibaseException {
                        if (changeSet.getLabels().getLabels().contains("release")) {
                            if (changeSet.getComments().equals(prevRelease.get())) {
                                active.set(true);
                            } else if (changeSet.getComments().equals(lastRelease.get())) {
                                active.set(false);
                            }
                        } else if (active.get()) {
                            ChangeSet.ExecType execType = changeSet.execute(databaseChangeLog, database);
                            database.markChangeSetExecStatus(changeSet, execType);
                            database.commit();
                        }
                    }
                }, new RuntimeEnvironment(database, new Contexts(), new LabelExpression()));
                output.flush();
                output.close();
            }


            {
                FileWriter output = new FileWriter(new File(dir, "" + lastRelease.get() + ".rollback.sql"));

                LoggingExecutor loggingExecutor = new LoggingExecutor(ExecutorService.getInstance().getExecutor(database), output, database);
                ExecutorService.getInstance().setExecutor(database, loggingExecutor);

                changeLogIterator.run(new RollbackVisitor(database, null) {
                    @Override
                    public void visit(ChangeSet changeSet, DatabaseChangeLog databaseChangeLog, Database database, Set<ChangeSetFilterResult> filterResults) throws LiquibaseException {
                        if (changeSet.getLabels().getLabels().contains("release")) {
                            if (changeSet.getComments().equals(lastRelease.get())) {
                                active.set(true);
                            } else if (changeSet.getComments().equals(prevRelease.get())) {
                                active.set(false);
                            }
                        } else if (active.get()) {
                            super.visit(changeSet, databaseChangeLog, database, filterResults);
                        }
                    }
                }, new RuntimeEnvironment(database, new Contexts(), new LabelExpression()));
                output.flush();
                output.close();
            }

        } catch (LiquibaseException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
