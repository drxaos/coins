package com.github.drxaos.coins.controller.category;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.events.ApplicationStart;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.application.validation.ValidationException;
import com.github.drxaos.coins.controller.JsonTransformer;
import com.github.drxaos.coins.domain.Category;
import com.github.drxaos.coins.domain.User;
import com.j256.ormlite.dao.Dao;
import spark.Spark;

import java.util.List;
import java.util.Objects;

public class CategoryController implements ApplicationStart {

    @Autowire
    JsonTransformer json;

    @Autowire
    Db db;

    public static final String CONTEXT = "/api/v1/categories";

    @Override
    public void onApplicationStart(Application application) throws ApplicationInitializationException {

        // create
        Spark.post(CONTEXT, (request, response) -> {
            User user = request.session().attribute("user");

            Category category;
            try {
                category = json.parse(request.body(), Category.class)
                        .id(null)
                        .user(user)
                        .save();
            } catch (ValidationException e) {
                response.status(400);
                return e.getValidationResult();
            } catch (TypedSqlException e) {
                if (e.getType() == TypedSqlException.Type.CONFLICT) {
                    response.status(409);
                    return "duplicate-entity";
                } else {
                    response.status(400);
                    return "invalid-entity";
                }
            }

            response.status(201); // 201 Created
            return category.id();
        }, json);

        // get
        Spark.get(CONTEXT + "/:id", (request, response) -> {
            User user = request.session().attribute("user");
            Dao<Category, Long> categories = db.getDao(Category.class);
            Category category = categories.queryForId(Long.parseLong(request.params(":id")));
            if (category != null && Objects.equals(category.user().id(), user.id())) {
                return category;
            } else {
                response.status(404); // 404 Not found
                return "Category not found";
            }
        }, json);

        // update
        Spark.put(CONTEXT + "/:id", (request, response) -> {
            User user = request.session().attribute("user");
            Dao<Category, Long> categories = db.getDao(Category.class);
            Long id = Long.parseLong(request.params(":id"));
            Category category = categories.queryForId(Long.parseLong(request.params(":id")));
            if (category != null && Objects.equals(category.user().id(), user.id())) {

                try {
                    json.parse(request.body(), Category.class)
                            .id(category.id())
                            .user(user)
                            .save();
                } catch (ValidationException e) {
                    response.status(400);
                    return e.getValidationResult();
                } catch (TypedSqlException e) {
                    if (e.getType() == TypedSqlException.Type.CONFLICT) {
                        response.status(409);
                        return "duplicate-entity";
                    } else {
                        response.status(400);
                        return "invalid-entity";
                    }
                }

                return "Category with id " + id + " updated";
            } else {
                response.status(404); // 404 Not found
                return "Category not found";
            }
        }, json);

        // delete
        Spark.delete(CONTEXT + "/:id", (request, response) -> {
            User user = request.session().attribute("user");
            Dao<Category, Long> categories = db.getDao(Category.class);
            Long id = Long.parseLong(request.params(":id"));
            Category category = categories.queryForId(Long.parseLong(request.params(":id")));
            if (category != null && Objects.equals(category.user().id(), user.id())) {

                try {
                    category.delete();
                } catch (TypedSqlException e) {
                    if (e.getType() == TypedSqlException.Type.CONFLICT) {
                        response.status(409);
                        return "duplicate-entity";
                    } else {
                        response.status(400);
                        return "invalid-entity";
                    }
                }

                return "Category with id " + id + " deleted";
            } else {
                response.status(404); // 404 Not found
                return "Category not found";
            }
        }, json);

        // list
        Spark.get(CONTEXT, (request, response) -> {
            User user = request.session().attribute("user");
            Dao<Category, Long> categories = db.getDao(Category.class);
            List<Category> categoryList = categories.queryBuilder()
                    .orderBy("id", true)
                    .where().eq("user_id", user)
                    .query();
            return categoryList;
        }, json);

    }
}
