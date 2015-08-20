package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.events.ApplicationInit;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.utils.DateUtil;
import com.j256.ormlite.dao.Dao;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
public class InitialData implements ApplicationInit {

    @Inject
    Db db;

    @Inject
    DateUtil dateUtil;

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {
        try {
            db.callInTransaction(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Dao<User, Long> users = db.getDao(User.class);
                    List<User> userList = users.queryForFieldValues(Collections.<String, Object>singletonMap("name", "test"));
                    if (userList.isEmpty()) {
                        User user = new User()
                                .name("test")
                                .email("test@example.com")
                                .password("test")
                                .lang(User.LANG_EN)
                                .save();
                        log.info(user.toString());

                        Category salaryCategory = new Category()
                                .user(user)
                                .name("Salary")
                                .income(true)
                                .save();
                        log.info(salaryCategory.toString());

                        Category foodCategory = new Category()
                                .user(user)
                                .name("Food")
                                .expense(true)
                                .save();
                        log.info(foodCategory.toString());

                        Account walletAccount = new Account()
                                .user(user)
                                .name("Wallet")
                                .type(Account.Type.CASH)
                                .currency("USD")
                                .value(new BigDecimal(0))
                                .created(dateUtil.parseDateTime("01.01.2000", "00:00:00"))
                                .save();
                        log.info(walletAccount.toString());

                        Tx salaryTx = new Tx()
                                .user(user)
                                .date(dateUtil.parseDateTime("01.01.2000", "12:00:00"))
                                .category(salaryCategory)
                                .incomeAccount(walletAccount)
                                .incomeValue(new BigDecimal(100))
                                .comment("Whee! Got salary!")
                                .save();
                        log.info(salaryTx.toString());

                        Tx foodTx = new Tx()
                                .user(user)
                                .date(dateUtil.parseDateTime("01.01.2000", "18:00:00"))
                                .category(foodCategory)
                                .outcomeAccount(walletAccount)
                                .outcomeValue(new BigDecimal(50))
                                .comment("Bought some food")
                                .save();
                        log.info(foodTx.toString());
                    }
                    return null;
                }
            });

        } catch (SQLException e) {
            throw new ApplicationInitializationException("Bootstrap error", e);
        }

    }
}