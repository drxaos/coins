package com.github.drxaos.coins.controller.account;

import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.database.OptimisticLockException;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.application.validation.ValidationException;
import com.github.drxaos.coins.controller.AbstractRestController;
import com.github.drxaos.coins.controller.AbstractRestPublisher;
import com.github.drxaos.coins.controller.Publish;
import com.github.drxaos.coins.controller.PublishingContext;
import com.github.drxaos.coins.controller.crud.*;
import com.github.drxaos.coins.domain.Account;
import com.github.drxaos.coins.domain.User;
import com.github.drxaos.coins.service.tx.TxService;
import com.github.drxaos.coins.utils.DateUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@PublishingContext("/api/v1/accounts")
public class AccountController extends AbstractRestController {

    @Inject
    DateUtil dateUtil;

    @Inject
    TxService txService;

    @Inject
    Db db;

    protected boolean checkAccountExists(Account entity, User user) throws CrudException {
        try {
            Dao<Account, Long> dao = db.getDao(Account.class);
            QueryBuilder<Account, Long> qb = dao.queryBuilder();
            qb.where().eq("user_id", user.id())
                    .and().eq("name", entity.name())
                    .and().isNull("closed");
            Account found = qb.queryForFirst();
            return found != null;
        } catch (TypedSqlException | SQLException e) {
            throw new CrudException(500, "Server error", null);
        }
    }

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.POST, path = "")
    public final CrudCreateRoute<Account> create = new CrudCreateRoute<Account>(Account.class) {
        @Override
        protected Account process(Account entity) throws CrudException {
            if (checkAccountExists(entity, transport.loggedInUser())) {
                throw new CrudException(409, "duplicate-entity", sqlExceptionData(entity, null));
            }
            return entity
                    .user(transport.loggedInUser())
                    .created(dateUtil.now())
                    .startValue(BigDecimal.ZERO);
        }

        @Override
        protected Object sqlExceptionData(Account entity, TypedSqlException e) throws CrudException {
            return Collections.singletonMap("name", entity.name());
        }
    };

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.GET, path = "/:id")
    public final CrudGetRoute<Account> get = new CrudGetRoute<Account>(Account.class) {
        @Override
        protected Account process(Account entity) throws CrudException {
            if (!Objects.equals(entity.user().id(), transport.loggedInUser().id()) || entity.closed() != null) {
                throw new CrudException(404, "not-found", Collections.singletonMap("id", entity.id()));
            }
            try {
                entity.value(txService.currentValue(entity));
            } catch (TypedSqlException e) {
                throw new CrudException(500, "Server error", null);
            }
            return entity;
        }
    };

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.PUT, path = "/:id")
    public final CrudUpdateRoute<Account> update = new CrudUpdateRoute<Account>(Account.class) {
        @Override
        protected Account process(Account oldEntity, Account newEntity) throws CrudException {
            if (!Objects.equals(oldEntity.user().id(), transport.loggedInUser().id()) || oldEntity.closed() != null) {
                throw new CrudException(404, "not-found", Collections.singletonMap("id", oldEntity.id()));
            }
            if (checkAccountExists(newEntity, transport.loggedInUser())) {
                throw new CrudException(409, "duplicate-entity", sqlExceptionData(newEntity, null));
            }
            return newEntity
                    .user(oldEntity.user())
                    .startValue(oldEntity.startValue())
                    .closed(oldEntity.closed())
                    .created(oldEntity.created());
        }

        @Override
        protected Object sqlExceptionData(Account entity, TypedSqlException e) throws CrudException {
            return Collections.singletonMap("name", entity.name());
        }
    };

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.DELETE, path = "/:id")
    public final CrudDeleteRoute<Account> delete = new CrudDeleteRoute<Account>(Account.class) {
        @Override
        protected void action(Account entity) throws TypedSqlException, OptimisticLockException, ValidationException {
            entity.closed(dateUtil.now());
            entity.save();
        }

        @Override
        protected void process(Account entity) throws CrudException {
            if (!Objects.equals(entity.user().id(), transport.loggedInUser().id()) || entity.closed() != null) {
                throw new CrudException(404, "not-found", Collections.singletonMap("id", entity.id()));
            }
        }

        @Override
        protected Object sqlExceptionData(Account entity, TypedSqlException e) throws CrudException {
            return Collections.singletonMap("name", entity.name());
        }
    };

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.GET, path = "")
    public final CrudListRoute<Account> list = new CrudListRoute<Account>(Account.class) {
        @Override
        protected void processQuery(QueryBuilder<Account, Long> queryBuilder) throws CrudException {
            try {
                queryBuilder
                        .orderBy("id", true)
                        .where().eq("user_id", transport.loggedInUser())
                        .and().isNull("closed");
            } catch (SQLException e) {
                throw new CrudException(500, "Server error", null);
            }
        }

        @Override
        protected List<Account> processList(List<Account> list) throws CrudException {
            for (Account account : list) {
                try {
                    account.value(txService.currentValue(account));
                } catch (TypedSqlException e) {
                    throw new CrudException(500, "Server error", null);
                }
            }
            return list;
        }
    };

}
