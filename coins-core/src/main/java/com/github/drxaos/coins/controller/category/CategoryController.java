package com.github.drxaos.coins.controller.category;

import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.controller.AbstractRestController;
import com.github.drxaos.coins.controller.AbstractRestPublisher;
import com.github.drxaos.coins.controller.Publish;
import com.github.drxaos.coins.controller.PublishingContext;
import com.github.drxaos.coins.controller.crud.*;
import com.github.drxaos.coins.domain.Category;
import com.j256.ormlite.stmt.QueryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@PublishingContext("/api/v1/categories")
public class CategoryController extends AbstractRestController {

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.POST, path = "")
    public final CrudCreateRoute<Category> create = new CrudCreateRoute<Category>(Category.class) {
        @Override
        protected Category process(Category entity) throws CrudException {
            return entity.user(transport.loggedInUser());
        }

        @Override
        protected Object sqlExceptionData(Category entity, TypedSqlException e) throws CrudException {
            return Collections.singletonMap("name", entity.name());
        }
    };

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.GET, path = "/:id")
    public final CrudGetRoute<Category> get = new CrudGetRoute<Category>(Category.class) {
        @Override
        protected Category process(Category entity) throws CrudException {
            if (!Objects.equals(entity.user().id(), transport.loggedInUser().id())) {
                throw new CrudException(404, "not-found", Collections.singletonMap("id", entity.id()));
            }
            return entity;
        }
    };

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.PUT, path = "/:id")
    public final CrudUpdateRoute<Category> update = new CrudUpdateRoute<Category>(Category.class) {
        @Override
        protected Category process(Category oldEntity, Category newEntity) throws CrudException {
            if (!Objects.equals(oldEntity.user().id(), transport.loggedInUser().id())) {
                throw new CrudException(404, "not-found", Collections.singletonMap("id", oldEntity.id()));
            }
            return newEntity.user(oldEntity.user());
        }

        @Override
        protected Object sqlExceptionData(Category entity, TypedSqlException e) throws CrudException {
            return Collections.singletonMap("name", entity.name());
        }
    };

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.DELETE, path = "/:id")
    public final CrudDeleteRoute<Category> delete = new CrudDeleteRoute<Category>(Category.class) {
        @Override
        protected void process(Category entity) throws CrudException {
            if (!Objects.equals(entity.user().id(), transport.loggedInUser().id())) {
                throw new CrudException(404, "not-found", Collections.singletonMap("id", entity.id()));
            }
        }

        @Override
        protected Object sqlExceptionData(Category entity, TypedSqlException e) throws CrudException {
            return Collections.singletonMap("name", entity.name());
        }
    };

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.GET, path = "")
    public final CrudListRoute<Category> list = new CrudListRoute<Category>(Category.class) {
        @Override
        protected void processQuery(QueryBuilder<Category, Long> queryBuilder) throws CrudException {
            try {
                queryBuilder
                        .orderBy("id", true)
                        .where().eq("user_id", transport.loggedInUser());
            } catch (SQLException e) {
                throw new CrudException(500, "Server error", null);
            }
        }

        @Override
        protected List<Category> processList(List<Category> list) throws CrudException {
            return list;
        }
    };

}
