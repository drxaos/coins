package com.github.drxaos.coins.controller.category;

import com.github.drxaos.coins.application.*;
import com.github.drxaos.coins.controller.JsonTransformer;
import com.github.drxaos.coins.domain.Category;
import com.github.drxaos.coins.domain.User;
import com.j256.ormlite.dao.Dao;
import spark.Spark;

import java.util.List;

public class CategoryController implements ApplicationStart {

    @Autowire
    JsonTransformer json;

    @Autowire
    Db db;

    @Override
    public void onApplicationStart(Application application) throws ApplicationInitializationException {

        // create
        Spark.post("/categories", (request, response) -> {
            User user = request.session().attribute("user");

            Category category = new Category()
                    .user(user)
                    .name(request.queryParams("name"))
                    .expense(request.queryParams("expense") != null)
                    .income(request.queryParams("income") != null)
                    .save();

            response.status(201); // 201 Created
            return category.id();
        }, json);

        // get
        Spark.get("/categories/:id", (request, response) -> {
            User user = request.session().attribute("user");
            Dao<Category, Long> categories = db.getDao(Category.class);
            Category category = categories.queryForId(Long.parseLong(request.params(":id")));
            if (category != null && category.user().equals(user)) {
                return category;
            } else {
                response.status(404); // 404 Not found
                return "Category not found";
            }
        }, json);

        // update
        Spark.put("/categories/:id", (request, response) -> {
            User user = request.session().attribute("user");
            Dao<Category, Long> categories = db.getDao(Category.class);
            Long id = Long.parseLong(request.params(":id"));
            Category category = categories.queryForId(Long.parseLong(request.params(":id")));
            if (category != null && category.user().equals(user)) {
                String newName = request.queryParams("name");
                String newExpense = request.queryParams("expense");
                String newIncome = request.queryParams("income");
                if (newName != null) {
                    category.name(newName);
                }
                if (newExpense != null) {
                    category.expense(newExpense.equals("true"));
                }
                if (newIncome != null) {
                    category.income(newIncome.equals("true"));
                }

                return "Category with id '" + id + "' updated";
            } else {
                response.status(404); // 404 Not found
                return "Category not found";
            }
        }, json);

        // delete
        Spark.delete("/categories/:id", (request, response) -> {
            User user = request.session().attribute("user");
            Dao<Category, Long> categories = db.getDao(Category.class);
            Long id = Long.parseLong(request.params(":id"));
            Category category = categories.queryForId(Long.parseLong(request.params(":id")));
            if (category != null && category.user().equals(user)) {
                category.delete();
                return "Category with id '" + id + "' deleted";
            } else {
                response.status(404); // 404 Not found
                return "Category not found";
            }
        }, json);

        // list
        Spark.get("/categories", (request, response) -> {
            User user = request.session().attribute("user");
            Dao<Category, Long> categories = db.getDao(Category.class);
            List<Category> categoryList = categories.queryForEq("user", user);
            return categoryList;
        }, json);

    }
}
