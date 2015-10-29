package com.github.drxaos.coins.test;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.database.h2.H2Db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.SQLException;

public class H2DbHelper extends H2Db {

    static String testName = "test";
    static String runName = "testdb";

    @Override
    public String getJdbcUrl() {
        return "jdbc:h2:file:./" + runName + "/" + testName;
    }

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {
        if (testName.equals("test")) {
            setCreateSchema(true);
            setCheckSchema(true);
        } else {
            setCreateSchema(false);
            setCheckSchema(false);
        }
        super.onApplicationInit(application);
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

    public static void clearState(boolean init) {
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
