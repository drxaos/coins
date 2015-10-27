package com.github.drxaos.coins.controller.transactions;

import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.controller.AbstractRestController;
import com.github.drxaos.coins.controller.AbstractRestPublisher;
import com.github.drxaos.coins.controller.Publish;
import com.github.drxaos.coins.controller.PublishingContext;
import com.github.drxaos.coins.controller.crud.*;
import com.github.drxaos.coins.domain.Tx;
import com.j256.ormlite.stmt.QueryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@PublishingContext("/api/v1/transactions")
public class TxController extends AbstractRestController {

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.POST, path = "")
    public final CrudCreateRoute<Tx> create = new CrudCreateRoute<Tx>(Tx.class) {
        @Override
        protected Tx process(Tx entity) throws CrudException {
            return entity.user(transport.loggedInUser());
        }

        @Override
        protected Object sqlExceptionData(Tx entity, TypedSqlException e) throws CrudException {
            return Collections.singletonMap("name", entity.category());
        }
    };

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.GET, path = "/:id")
    public final CrudGetRoute<Tx> get = new CrudGetRoute<Tx>(Tx.class) {
        @Override
        protected Tx process(Tx entity) throws CrudException {
            if (!Objects.equals(entity.user().id(), transport.loggedInUser().id())) {
                throw new CrudException(404, "not-found", Collections.singletonMap("id", entity.id()));
            }
            return entity;
        }
    };

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.PUT, path = "/:id")
    public final CrudUpdateRoute<Tx> update = new CrudUpdateRoute<Tx>(Tx.class) {
        @Override
        protected Tx process(Tx oldEntity, Tx newEntity) throws CrudException {
            if (!Objects.equals(oldEntity.user().id(), transport.loggedInUser().id())) {
                throw new CrudException(404, "not-found", Collections.singletonMap("id", oldEntity.id()));
            }
            return newEntity.user(oldEntity.user());
        }

        @Override
        protected Object sqlExceptionData(Tx entity, TypedSqlException e) throws CrudException {
            return Collections.singletonMap("name", entity.name());
        }
    };

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.DELETE, path = "/:id")
    public final CrudDeleteRoute<Tx> delete = new CrudDeleteRoute<Tx>(Tx.class) {
        @Override
        protected void process(Tx entity) throws CrudException {
            if (!Objects.equals(entity.user().id(), transport.loggedInUser().id())) {
                throw new CrudException(404, "not-found", Collections.singletonMap("id", entity.id()));
            }
        }

        @Override
        protected Object sqlExceptionData(Tx entity, TypedSqlException e) throws CrudException {
            return Collections.singletonMap("name", entity.name());
        }
    };

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.GET, path = "")
    public final CrudListRoute<Tx> list = new CrudListRoute<Tx>(Tx.class) {
        @Override
        protected void processQuery(QueryBuilder<Tx, Long> queryBuilder) throws CrudException {
            try {
                queryBuilder
                        .orderBy("id", true)
                        .where().eq("user_id", transport.loggedInUser());
            } catch (SQLException e) {
                throw new CrudException(500, "Server error", null);
            }
        }

        @Override
        protected List<Tx> processList(List<Tx> list) throws CrudException {
            return list;
        }
    };

}
