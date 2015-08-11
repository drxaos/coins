package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.*;
import com.github.drxaos.coins.utils.DateUtil;
import com.j256.ormlite.dao.Dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class InitialData implements ApplicationInit {

    @Autowire
    Database db;

    @Autowire
    DateUtil dateUtil;

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {
        try {
            db.callInTransaction(() -> {
                Dao<User, Long> users = db.getDao(User.class);
                List<User> userList = users.queryForFieldValues(Collections.singletonMap("name", "test"));
                if (userList.isEmpty()) {
                    User user = new User();
                    user.setName("test");
                    user.setPassword("test");
                    user.create();

                    Category salaryCategory = new Category(user, "salary", false, true);
                    salaryCategory.create();

                    Category foodCategory = new Category(user, "food", true, false);
                    foodCategory.create();

                    Account walletAccount = new Account(user, "wallet", "USD", new BigDecimal(0));
                    walletAccount.create();

                    Tx salaryTx = new Tx();
                    salaryTx.setUser(user);
                    salaryTx.setDate(dateUtil.parseDateTime("01.01.2000", "12:00:00"));
                    salaryTx.setCategory(salaryCategory);
                    salaryTx.setIncomeAccount(walletAccount);
                    salaryTx.setIncomeValue(new BigDecimal(100));
                    salaryTx.setComment("Whee! Got salary!");
                    salaryTx.create();

                    Tx foodTx = new Tx();
                    foodTx.setUser(user);
                    foodTx.setDate(dateUtil.parseDateTime("01.01.2000", "18:00:00"));
                    foodTx.setCategory(foodCategory);
                    foodTx.setOutcomeAccount(walletAccount);
                    foodTx.setOutcomeValue(new BigDecimal(50));
                    foodTx.setComment("Bought some food");
                    foodTx.create();
                }
                return null;
            });

        } catch (SQLException e) {
            throw new ApplicationInitializationException("Bootstrap error", e);
        }

    }
}