package com.github.drxaos.coins.service.tx;

import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Component;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.domain.Account;
import com.github.drxaos.coins.domain.Tx;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.stmt.QueryBuilder;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.SQLException;

@Slf4j
@Component
public class TxService {

    @Inject
    Db db;

    public BigDecimal currentValue(Account account) throws TypedSqlException {
        try {
            Dao<Tx, Long> txes = db.getDao(Tx.class);
            BigDecimal incomeSum, outcomeSum;
            {
                QueryBuilder<Tx, Long> queryBuilder = txes.queryBuilder();
                queryBuilder
                        .selectRaw("SUM(incomeValue)").where().eq("incomeAccount_id", account.id()).queryRawFirst();
                GenericRawResults<Object[]> results = txes.queryRaw(queryBuilder.prepareStatementString(), new DataType[]{DataType.BIG_DECIMAL});
                incomeSum = (BigDecimal) results.getFirstResult()[0];
            }
            {
                QueryBuilder<Tx, Long> queryBuilder = txes.queryBuilder();
                queryBuilder
                        .selectRaw("SUM(outcomeValue)").where().eq("outcomeAccount_id", account.id()).queryRawFirst();
                GenericRawResults<Object[]> results = txes.queryRaw(queryBuilder.prepareStatementString(), new DataType[]{DataType.BIG_DECIMAL});
                outcomeSum = (BigDecimal) results.getFirstResult()[0];
            }
            return account.startValue()
                    .add(incomeSum != null ? incomeSum : BigDecimal.ZERO)
                    .subtract(outcomeSum != null ? outcomeSum : BigDecimal.ZERO);
        } catch (SQLException e) {
            TypedSqlException typedSqlException = new TypedSqlException(e, TypedSqlException.Type.UNKNOWN);
            log.error("server error", e);
            throw typedSqlException;
        } catch (TypedSqlException e) {
            log.error("server error", e);
            throw e;
        }
    }

}
