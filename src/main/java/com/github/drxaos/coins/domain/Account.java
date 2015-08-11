package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.Entity;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.sql.SQLException;

@DatabaseTable(tableName = "accounts")
public class Account extends Entity {
    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false, uniqueCombo = true, foreign = true)
    User user;
    @DatabaseField(canBeNull = false, uniqueCombo = true)
    String name;
    @DatabaseField(canBeNull = false)
    String currency;
    @DatabaseField(canBeNull = false)
    BigDecimal value;

    public Account() throws SQLException {
    }

    public Account(User user, String name, String currency, BigDecimal value) throws SQLException {
        this.user = user;
        this.name = name;
        this.currency = currency;
        this.value = value;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != null ? !id.equals(account.id) : account.id != null) return false;
        if (user != null ? !user.equals(account.user) : account.user != null) return false;
        if (name != null ? !name.equals(account.name) : account.name != null) return false;
        if (currency != null ? !currency.equals(account.currency) : account.currency != null) return false;
        return !(value != null ? !value.equals(account.value) : account.value != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", currency='" + currency + '\'' +
                ", value=" + value +
                '}';
    }
}
