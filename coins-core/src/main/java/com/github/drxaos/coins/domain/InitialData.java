package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.events.ApplicationInitEventListener;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.utils.DateUtil;
import com.j256.ormlite.dao.Dao;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
public class InitialData implements ApplicationInitEventListener {

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

                        Category otherCategory = new Category()
                                .user(user)
                                .name("Other")
                                .expense(true)
                                .save();
                        log.info(otherCategory.toString());

                        Account walletAccount = new Account()
                                .user(user)
                                .name("Wallet")
                                .type(Account.Type.CASH)
                                .currency("USD")
                                .value(new BigDecimal(0))
                                .created(dateUtil.parseDate("01.01.2000"))
                                .save();
                        log.info(walletAccount.toString());

                        Tx salaryTx = new Tx()
                                .user(user)
                                .date(dateUtil.parseDate("02.01.2000"))
                                .category(salaryCategory)
                                .incomeAccount(walletAccount)
                                .incomeValue(new BigDecimal(500))
                                .comment("Whee! Got salary!")
                                .save();
                        log.info(salaryTx.toString());

                        Tx foodTx = new Tx()
                                .user(user)
                                .date(dateUtil.parseDate("03.01.2000"))
                                .category(foodCategory)
                                .outcomeAccount(walletAccount)
                                .outcomeValue(new BigDecimal(50))
                                .comment("Bought some food")
                                .save();
                        log.info(foodTx.toString());

                        Date date = dateUtil.parseDate("03.01.2000");
                        BigDecimal val = new BigDecimal(450);
                        String[] comments = {"Food", "Gasoline", "Cinema", "Cafe"};
                        Category[] categories = {foodCategory, otherCategory, otherCategory, foodCategory};
                        for (int i = 0; i < 50; i++) {
                            date = new Date(date.getTime() + 1000 * 60 * 60 * 24 * (Math.round(Math.random() * 10)));
                            BigDecimal spent = new BigDecimal(Math.round(Math.random() * 100));
                            int n = (int) (Math.random() * 4);
                            Tx genericTx = new Tx()
                                    .user(user)
                                    .date(date)
                                    .category(categories[n])
                                    .outcomeAccount(walletAccount)
                                    .outcomeValue(spent)
                                    .comment(comments[n])
                                    .save();
                            log.info(genericTx.toString());

                            val = val.subtract(spent);
                            if (val.signum() < 0) {
                                BigDecimal salary = new BigDecimal(500);
                                Tx salary2Tx = new Tx()
                                        .user(user)
                                        .date(date)
                                        .category(salaryCategory)
                                        .incomeAccount(walletAccount)
                                        .incomeValue(salary)
                                        .comment("Salary")
                                        .save();
                                log.info(salary2Tx.toString());
                                val = val.add(salary);
                            }
                        }
                    }
                    return null;
                }
            });

        } catch (SQLException e) {
            throw new ApplicationInitializationException("Bootstrap error", e);
        }

    }
}