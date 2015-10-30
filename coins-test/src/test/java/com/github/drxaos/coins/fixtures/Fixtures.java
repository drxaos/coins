package com.github.drxaos.coins.fixtures;

import java.util.HashMap;
import java.util.Map;

public class Fixtures {

    static Map<Class<? extends Fixtures>, Fixtures> fixtures = new HashMap<>();

    public static <T extends Fixtures> T require(Class<T> fixtureType) {
        T fixture = (T) fixtures.get(fixtureType);
        if (fixture == null) {
            try {
                fixture = fixtureType.newInstance();
                fixtures.put(fixtureType, fixture);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new FixturesLoadingException("cannot instantiate fixture: " + fixtureType, e);
            }
        }
        return fixture;
    }

    public static <T extends Fixtures> T add(T fixture) {
        if (!fixtures.containsKey(fixture.getClass())) {
            fixtures.put(fixture.getClass(), fixture);
        }
        return fixture;
    }
}
