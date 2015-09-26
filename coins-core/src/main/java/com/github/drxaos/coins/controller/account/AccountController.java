package com.github.drxaos.coins.controller.account;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.database.OptimisticLockException;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.events.ApplicationStart;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.application.validation.ValidationException;
import com.github.drxaos.coins.controller.AbstractRestPublisher;
import com.github.drxaos.coins.controller.crud.*;
import com.github.drxaos.coins.domain.Account;
import com.github.drxaos.coins.service.tx.TxService;
import com.github.drxaos.coins.utils.DateUtil;
import com.j256.ormlite.stmt.QueryBuilder;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
public class AccountController implements ApplicationStart {

    public static final String CONTEXT = "/api/v1/accounts";

    @Inject
    AbstractRestPublisher publisher;

    @Inject
    DateUtil dateUtil;

    @Inject
    TxService txService;

    @Override
    public void onApplicationStart(Application application) throws ApplicationInitializationException {
        publisher.publish(AbstractRestPublisher.Method.POST, CONTEXT, create);
        publisher.publish(AbstractRestPublisher.Method.GET, CONTEXT + "/:id", get);
        publisher.publish(AbstractRestPublisher.Method.PUT, CONTEXT + "/:id", update);
        publisher.publish(AbstractRestPublisher.Method.DELETE, CONTEXT + "/:id", delete);
        publisher.publish(AbstractRestPublisher.Method.GET, CONTEXT, list);
    }

    @Autowire
    public final CrudCreateRoute<Account> create = new CrudCreateRoute<Account>(Account.class) {
        @Override
        protected Account process(Account entity) throws CrudException {
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
    public final CrudUpdateRoute<Account> update = new CrudUpdateRoute<Account>(Account.class) {
        @Override
        protected Account process(Account oldEntity, Account newEntity) throws CrudException {
            if (!Objects.equals(oldEntity.user().id(), transport.loggedInUser().id()) || oldEntity.closed() != null) {
                throw new CrudException(404, "not-found", Collections.singletonMap("id", oldEntity.id()));
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
