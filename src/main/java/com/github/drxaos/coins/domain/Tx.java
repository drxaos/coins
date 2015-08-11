package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.Entity;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

@DatabaseTable(tableName = "transactions")
public class Tx extends Entity {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false, foreign = true)
    User user;
    @DatabaseField(canBeNull = false)
    Date date;
    @DatabaseField(canBeNull = true, foreign = true)
    Category category;

    @DatabaseField(canBeNull = true, foreign = true)
    Account outcomeAccount;
    @DatabaseField(canBeNull = true)
    BigDecimal outcomeValue;

    @DatabaseField(canBeNull = true, foreign = true)
    Account incomeAccount;
    @DatabaseField(canBeNull = true)
    BigDecimal incomeValue;

    @DatabaseField(canBeNull = true)
    String comment;

    public Tx() throws SQLException {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Account getOutcomeAccount() {
        return outcomeAccount;
    }

    public void setOutcomeAccount(Account outcomeAccount) {
        this.outcomeAccount = outcomeAccount;
    }

    public BigDecimal getOutcomeValue() {
        return outcomeValue;
    }

    public void setOutcomeValue(BigDecimal outcomeValue) {
        this.outcomeValue = outcomeValue;
    }

    public Account getIncomeAccount() {
        return incomeAccount;
    }

    public void setIncomeAccount(Account incomeAccount) {
        this.incomeAccount = incomeAccount;
    }

    public BigDecimal getIncomeValue() {
        return incomeValue;
    }

    public void setIncomeValue(BigDecimal incomeValue) {
        this.incomeValue = incomeValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tx tx = (Tx) o;

        if (id != null ? !id.equals(tx.id) : tx.id != null) return false;
        if (user != null ? !user.equals(tx.user) : tx.user != null) return false;
        if (date != null ? !date.equals(tx.date) : tx.date != null) return false;
        if (category != null ? !category.equals(tx.category) : tx.category != null) return false;
        if (outcomeAccount != null ? !outcomeAccount.equals(tx.outcomeAccount) : tx.outcomeAccount != null)
            return false;
        if (outcomeValue != null ? !outcomeValue.equals(tx.outcomeValue) : tx.outcomeValue != null) return false;
        if (incomeAccount != null ? !incomeAccount.equals(tx.incomeAccount) : tx.incomeAccount != null) return false;
        if (incomeValue != null ? !incomeValue.equals(tx.incomeValue) : tx.incomeValue != null) return false;
        return !(comment != null ? !comment.equals(tx.comment) : tx.comment != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (outcomeAccount != null ? outcomeAccount.hashCode() : 0);
        result = 31 * result + (outcomeValue != null ? outcomeValue.hashCode() : 0);
        result = 31 * result + (incomeAccount != null ? incomeAccount.hashCode() : 0);
        result = 31 * result + (incomeValue != null ? incomeValue.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tx{" +
                "id=" + id +
                ", user=" + user +
                ", date=" + date +
                ", category=" + category +
                ", outcomeAccount=" + outcomeAccount +
                ", outcomeValue=" + outcomeValue +
                ", incomeAccount=" + incomeAccount +
                ", incomeValue=" + incomeValue +
                ", comment='" + comment + '\'' +
                '}';
    }
}
