package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.Entity;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

@DatabaseTable(tableName = "categories")
public class Category extends Entity {
    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false, uniqueCombo = true, foreign = true)
    User user;

    @DatabaseField(canBeNull = false, uniqueCombo = true)
    String name;

    @DatabaseField(canBeNull = false)
    boolean expense;

    @DatabaseField(canBeNull = false)
    boolean income;

    public Category() throws SQLException {
    }

    public Category(User user, String name, boolean expense, boolean income) throws SQLException {
        this.user = user;
        this.name = name;
        this.expense = expense;
        this.income = income;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExpense() {
        return expense;
    }

    public void setExpense(boolean expense) {
        this.expense = expense;
    }

    public boolean isIncome() {
        return income;
    }

    public void setIncome(boolean income) {
        this.income = income;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (expense != category.expense) return false;
        if (income != category.income) return false;
        if (id != null ? !id.equals(category.id) : category.id != null) return false;
        if (user != null ? !user.equals(category.user) : category.user != null) return false;
        return !(name != null ? !name.equals(category.name) : category.name != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (expense ? 1 : 0);
        result = 31 * result + (income ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", expense=" + expense +
                ", income=" + income +
                '}';
    }
}
