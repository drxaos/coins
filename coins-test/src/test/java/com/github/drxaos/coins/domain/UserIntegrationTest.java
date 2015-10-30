package com.github.drxaos.coins.domain;

import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.database.TypedSqlException;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.application.validation.ValidationResult;
import com.github.drxaos.coins.test.IntegrationTestCase;
import com.j256.ormlite.dao.Dao;
import org.junit.Test;

import java.util.List;

public class UserIntegrationTest extends IntegrationTestCase {

    @Inject
    Db db;

    @Test
    public void test_save() throws Exception {

        Dao<User, Long> dao = db.getDao(User.class);
        List<User> users = dao.queryForAll();
        assertEquals(0, users.size());

        User user1 = new User()
                .email("user1@example.com")
                .lang("RU")
                .name("user1")
                .password("password1")
                .save();

        User user2 = new User()
                .email("user2@example.com")
                .lang("RU")
                .name("user2")
                .password("password2")
                .save();

        users = dao.queryForAll();
        assertEquals(2, users.size());
    }

    @Test
    public void test_unique() throws Exception {

        Dao<User, Long> dao = db.getDao(User.class);
        List<User> users = dao.queryForAll();
        assertEquals(0, users.size());

        User user1 = new User()
                .email("user1@example.com")
                .lang("RU")
                .name("user1")
                .password("password1")
                .save();

        TypedSqlException e = shouldFail(TypedSqlException.class, () ->
                        new User()
                                .email("user2@example.com")
                                .lang("RU")
                                .name("user1")
                                .password("password2")
                                .save()
        );
        assertEquals(TypedSqlException.Type.CONFLICT, e.getType());
    }

    @Test
    public void test_validate() throws Exception {

        Dao<User, Long> dao = db.getDao(User.class);
        List<User> users = dao.queryForAll();
        assertEquals(0, users.size());

        User user1 = new User()
                .email("user1@example.com")
                .lang("RU")
                .name("user1")
                .password("password1")
                .save();

        ValidationResult<User> v = user1.validate();
        assertFalse(v.hasErrors());

        user1.email("");
        v = user1.validate();
        assertTrue(v.hasErrors());
        assertEquals("email", v.getErrors().get(0).fieldName);

        user1.email("a");
        v = user1.validate();
        assertTrue(v.hasErrors());
        assertEquals("email", v.getErrors().get(0).fieldName);

        user1.email("a@");
        v = user1.validate();
        assertFalse(v.hasErrors());

        user1.email("user1@example.com");
        v = user1.validate();
        assertFalse(v.hasErrors());

        user1.name("");
        v = user1.validate();
        assertTrue(v.hasErrors());
        assertEquals("name", v.getErrors().get(0).fieldName);

        user1.name("a");
        v = user1.validate();
        assertTrue(v.hasErrors());
        assertEquals("name", v.getErrors().get(0).fieldName);
    }
}
