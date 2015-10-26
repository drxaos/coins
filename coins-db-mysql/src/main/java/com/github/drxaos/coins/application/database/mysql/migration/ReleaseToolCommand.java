package com.github.drxaos.coins.application.database.mysql.migration;

import liquibase.Liquibase;
import liquibase.RuntimeEnvironment;
import liquibase.changelog.ChangeLogIterator;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.changelog.filter.ChangeSetFilter;
import liquibase.changelog.filter.ChangeSetFilterResult;
import liquibase.changelog.visitor.ChangeSetVisitor;
import liquibase.database.Database;
import liquibase.exception.LiquibaseException;
import liquibase.integration.commandline.CommandLineResourceAccessor;
import liquibase.resource.CompositeResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.sdk.database.MockDatabase;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class ReleaseToolCommand extends ToolCommand {

    @Override
    public void execute() {

        try {
            FileSystemResourceAccessor fsOpener = new FileSystemResourceAccessor();
            CommandLineResourceAccessor clOpener = new CommandLineResourceAccessor(this.getClass().getClassLoader());
            CompositeResourceAccessor fileOpener = new CompositeResourceAccessor(fsOpener, clOpener);

            Liquibase liquibase = new Liquibase("migration/changelog.xml", fileOpener, new MockDatabase());
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
                }, new RuntimeEnvironment(null, null, null));
            }

            System.out.println("" + prevRelease.get() + " -> " + lastRelease.get());

            AtomicReference<Boolean> active = new AtomicReference<>();
            active.set(prevRelease.get() == null);

            changeLogIterator.run(new ChangeSetVisitor() {
                @Override
                public Direction getDirection() {
                    return Direction.FORWARD;
                }

                @Override
                public void visit(ChangeSet changeSet, DatabaseChangeLog databaseChangeLog, Database database, Set<ChangeSetFilterResult> filterResults) throws LiquibaseException {
                    if (changeSet.getLabels().getLabels().contains("release")) {
                        if (changeSet.getComments().equals(prevRelease.get())) {
                            active.set(true);
                        } else if (changeSet.getComments().equals(lastRelease.get())) {
                            active.set(false);
                        }
                    } else if (active.get()) {
                        System.out.println(changeSet.toString());
                    }
                }
            }, new RuntimeEnvironment(null, null, null));

        } catch (LiquibaseException e) {
            e.printStackTrace();
        }
    }
}
