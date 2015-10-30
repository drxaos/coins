package com.github.drxaos.coins.application.test;

import com.github.drxaos.coins.application.factory.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Fixtures {

    Map<String, Object> objects = new HashMap<>();

    Map<Class<? extends Fixture>, Fixture> fixtures = new HashMap<>();

    public void require(Class<? extends Fixture>... fixtureTypes) {
        for (Class<? extends Fixture> fixtureType : fixtureTypes) {
            require(fixtureType);
        }
    }

    public <T extends Fixture> T require(Class<T> fixtureType) {
        T fixture = (T) fixtures.get(fixtureType);
        if (fixture == null) {
            try {
                fixture = fixtureType.newInstance();
                fixture.setFixtures(this);
                fixture.init();
                fixtures.put(fixtureType, fixture);
            } catch (Exception e) {
                throw new FixturesLoadingException("cannot init fixture: " + fixtureType, e);
            }
        }
        return fixture;
    }

    public <T extends Fixture> T add(T fixture) {
        if (!fixtures.containsKey(fixture.getClass())) {
            try {
                fixture.setFixtures(this);
                fixture.init();
                fixtures.put(fixture.getClass(), fixture);
            } catch (Exception e) {
                throw new FixturesLoadingException("cannot init fixture: " + fixture.getClass(), e);
            }
        }
        return fixture;
    }

    public <T> T get(String name) {
        return (T) objects.get(name);
    }

    void put(String name, Object obj) {
        objects.put(name, obj);
    }
}
