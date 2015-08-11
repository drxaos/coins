package com.github.drxaos.coins.application;

public abstract class Application {

    public enum State {
        CREATED, INITIALIZED, STARTED, STOPPED
    }

    private State state = State.CREATED;
    private AutowiringFactory factory = new AutowiringFactory();

    public State getState() {
        return state;
    }

    public AutowiringFactory getFactory() {
        return factory;
    }

    public void addObjects(Class... classes) {
        factory.createObject(classes);
    }

    public void addClasses(Class... classes) {
        factory.registerClass(classes);
    }

    abstract public void init();

    public void start() throws ApplicationInitializationException {
        init();
        for (ApplicationInit obj : factory.sortByDependencies(factory.getObjectsByInterface(ApplicationInit.class))) {
            obj.onApplicationInit(this);
        }
        state = State.INITIALIZED;

        for (ApplicationStart obj : factory.sortByDependencies(factory.getObjectsByInterface(ApplicationStart.class))) {
            obj.onApplicationStart(this);
        }
        state = State.STARTED;
    }

    public void stop() throws ApplicationInitializationException {
        for (ApplicationStop obj : factory.sortByDependencies(factory.getObjectsByInterface(ApplicationStop.class))) {
            obj.onApplicationStop(this);
        }
        state = State.STOPPED;
    }

}
