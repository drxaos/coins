package com.github.drxaos.coins.controller.category;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationStart;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.controller.JsonTransformer;
import com.github.drxaos.coins.controller.crud.*;
import com.github.drxaos.coins.domain.Category;
import com.j256.ormlite.stmt.QueryBuilder;
import spark.Spark;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class CategoryController implements ApplicationStart {

    @Inject
    JsonTransformer json;

    public static final String CONTEXT = "/api/v1/categories";

    @Autowire
    public final CrudCreateRoute<Category> create = new CrudCreateRoute<Category>(Category.class) {
        @Override
        protected Category process(Category entity) throws CrudException {
            return entity.user(loggedInUser());
        }
    };

    @Autowire
    public final CrudGetRoute<Category> get = new CrudGetRoute<Category>(Category.class) {
        @Override
        protected Category process(Category entity) throws CrudException {
            if (entity == null || !Objects.equals(entity.user().id(), loggedInUser().id())) {
                throw new CrudException("Not found", 404);
            }
            return entity;
        }
    };

    @Autowire
    public final CrudUpdateRoute<Category> update = new CrudUpdateRoute<Category>(Category.class) {
        @Override
        protected Category process(Category oldEntity, Category newEntity) throws CrudException {
            if (oldEntity == null || !Objects.equals(oldEntity.user().id(), loggedInUser().id())) {
                throw new CrudException("Not found", 404);
            }
            return newEntity.user(oldEntity.user());
        }
    };
    @Autowire
    public final CrudDeleteRoute<Category> delete = new CrudDeleteRoute<Category>(Category.class) {
        @Override
        protected void process(Category entity) throws CrudException {
            if (entity == null || !Objects.equals(entity.user().id(), loggedInUser().id())) {
                throw new CrudException("Not found", 404);
            }
        }
    };
    @Autowire
    public final CrudListRoute<Category> list = new CrudListRoute<Category>(Category.class) {
        @Override
        protected void processQuery(QueryBuilder<Category, Long> queryBuilder) throws CrudException {
            try {
                queryBuilder
                        .orderBy("id", true)
                        .where().eq("user_id", loggedInUser());
            } catch (SQLException e) {
                throw new CrudException("Server error", 500);
            }
        }

        @Override
        protected List<Category> processList(List<Category> list) throws CrudException {
            return list;
        }
    };

    @Override
    public void onApplicationStart(Application application) throws ApplicationInitializationException {
        Spark.post(CONTEXT, create, json);
        Spark.get(CONTEXT + "/:id", get, json);
        Spark.put(CONTEXT + "/:id", update, json);
        Spark.delete(CONTEXT + "/:id", delete, json);
        Spark.get(CONTEXT, list, json);
    }
}
