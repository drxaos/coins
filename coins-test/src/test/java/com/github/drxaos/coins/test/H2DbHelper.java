package com.github.drxaos.coins.test;

import com.github.drxaos.coins.CoinsCoreModule;
import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.config.ApplicationProps;
import com.github.drxaos.coins.application.database.h2.CoinsDbH2Module;
import com.github.drxaos.coins.application.database.h2.H2Db;
import com.google.common.collect.FluentIterable;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

public class H2DbHelper extends H2Db {

    static String testName = "test";
    static String runName = "testdb";

    static final AtomicReference<Boolean> dbInitialized = new AtomicReference<>(false);

    public static class Config extends ApplicationProps {
        @Override
        public void onApplicationInit(Application application) throws ApplicationInitializationException {
        }
    }

    static void init() throws ApplicationInitializationException {
        synchronized (dbInitialized) {
            if (!dbInitialized.get()) {
                H2DbHelper.clear(true);

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
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:h2:file:./" + runName + "/" + testName;
    }

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {
        setCreateSchema(false);
        setCheckSchema(false);
        super.onApplicationInit(application);
    }

    public void executeMigration() {
        try {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(((JdbcDatabaseConnection) this.getConnectionSource().getReadWriteConnection()).getInternalConnection()));
            FileSystemResourceAccessor fsOpener = new FileSystemResourceAccessor();
            CommandLineResourceAccessor clOpener = new CommandLineResourceAccessor(this.getClass().getClassLoader());
            CompositeResourceAccessor fileOpener = new CompositeResourceAccessor(fsOpener, clOpener);

            Liquibase liquibase = new Liquibase("migration/changelog.xml", fileOpener, database);
            liquibase.update(new Contexts(), new LabelExpression("!release"));

        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
        }
    }

    public static void prepareState() throws SQLException, IOException, ApplicationInitializationException {
        File db = new File("./" + runName + "/test.mv.db");
        File copy = new File("./" + runName + "/" + testName + ".mv.db");
        copyFile(db, copy);
    }

    public static void removeState() throws SQLException, IOException, ApplicationInitializationException {
        File copy = new File("./" + runName + "/" + testName + ".mv.db");
        copy.delete();
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    static void delete(File f) {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        f.delete();
    }

    public static void clear(boolean init) {
        File folder = new File("./" + runName + "");
        delete(folder);
        if (init) {
            folder.mkdirs();
        }
    }

    public static boolean initialized() {
        File db = new File("./" + runName + "/test");
        return db.exists();
    }
}
