package com.github.drxaos.coins.application;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

@Component(dependencies = {DbConnection.class})
public class DbInitializer implements ApplicationInit {
    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {
        DbConnection dbConnection = application.getFactory().findObject(DbConnection.class);

        List<Class> domainClasses = application.getFactory().getClassesByAnnotation(DatabaseTable.class);
        for (Class domainClass : domainClasses) {
            try {
                if (!dbConnection.getDao(domainClass).isTableExists()) {
                    TableUtils.createTable(dbConnection.getConnectionSource(), domainClass);
                }
            } catch (SQLException e) {
                throw new ApplicationInitializationException("Cannot create table for " + domainClass, e);
            }
        }
    }
}
