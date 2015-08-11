package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.Autowire;
import com.github.drxaos.coins.application.Entity;
import com.github.drxaos.coins.service.user.PasswordService;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

@DatabaseTable(tableName = "users")
public class User extends Entity {

    @Autowire
    transient PasswordService passwordService;

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField(canBeNull = false, uniqueIndex = true)
    private String name;
    @DatabaseField(canBeNull = false)
    private String password;

    public User() throws SQLException {
    }

    public User(String name, String password) throws SQLException {
        this.name = name;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = passwordService.encode(password);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(passwordService.encode(password));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (passwordService != null ? !passwordService.equals(user.passwordService) : user.passwordService != null)
            return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        return !(password != null ? !password.equals(user.password) : user.password != null);

    }

    @Override
    public int hashCode() {
        int result = passwordService != null ? passwordService.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "passwordService=" + passwordService +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
