package com.github.drxaos.coins.application.test;

abstract public class Fixture {

    Fixtures fixtures;

    void setFixtures(Fixtures fixtures) {
        this.fixtures = fixtures;
    }

    protected <T extends Fixture> T require(Class<T> fixtureType) {
        return fixtures.require(fixtureType);
    }

    public <T> T get(String name) {
        return fixtures.get(name);
    }

    protected void put(String name, Object obj) {
        fixtures.put(name, obj);
    }

    protected abstract void init() throws Exception;
}
