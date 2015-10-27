package com.github.drxaos.coins.controller.transactions;

import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.controller.*;
import com.github.drxaos.coins.domain.Account;
import com.github.drxaos.coins.domain.Tx;
import com.j256.ormlite.stmt.QueryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Slf4j
@PublishingContext("/api/v1/charts")
public class ChartsController extends AbstractRestController {

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.GET, path = "/stock")
    public final RestHandler<Void, List<List<Object>>> get = new RestHandler<Void, List<List<Object>>>() {

        @Inject
        Db db;

        @Override
        public List<List<Object>> handle() throws Exception {
            BigDecimal moneyAmount = BigDecimal.ZERO;

            List<Account> accounts = db.getDao(Account.class).queryForEq("user_id", transport.loggedInUser().id());
            for (Account account : accounts) {
                moneyAmount = moneyAmount.add(account.startValue());
            }

            QueryBuilder<Tx, Long> qb = db.getDao(Tx.class).queryBuilder();
            qb.where().eq("user_id", transport.loggedInUser().id());
            qb.orderBy("date", true);
            List<Tx> txes = qb.query();

            List<List<Object>> data = new ArrayList<>();

            Iterator<Tx> it = txes.iterator();

            Date d = null;
            while (it.hasNext()) {
                Tx tx = it.next();

                if (!tx.date().equals(d)) {
                    pushPoint(data, d, moneyAmount);
                    d = tx.date();
                }

                moneyAmount = moneyAmount.add(tx.incomeValue() != null ? tx.incomeValue() : BigDecimal.ZERO)
                        .subtract(tx.outcomeValue() != null ? tx.outcomeValue() : BigDecimal.ZERO);
            }
            pushPoint(data, d, moneyAmount);

            return data;
        }

        private void pushPoint(List<List<Object>> list, Date date, BigDecimal value) {
            if (date != null) {
                ArrayList<Object> tuple = new ArrayList<>();
                tuple.add(date.getTime());
                tuple.add(value.doubleValue());
                list.add(tuple);
            }
        }
    };
}
