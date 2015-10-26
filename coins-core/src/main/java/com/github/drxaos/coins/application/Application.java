package com.github.drxaos.coins.application;

import com.github.drxaos.coins.application.events.ApplicationInit;
import com.github.drxaos.coins.application.events.ApplicationStart;
import com.github.drxaos.coins.application.events.ApplicationStop;
import com.github.drxaos.coins.application.factory.AutowiringFactory;

import java.util.Arrays;
import java.util.List;

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
        addObjects(Arrays.asList(classes));
    }

    public void addObjects(List<Class> classes) {
        factory.createObject(classes);
    }

    public void addClasses(Class... classes) {
        addClasses(Arrays.asList(classes));
    }

    public void addClasses(List<Class> classes) {
        factory.registerClass(classes);
    }

    abstract public void init() throws ApplicationInitializationException;

    public void start() throws ApplicationInitializationException {
        init();
        for (ApplicationInit obj : factory.getObjectsByInterface(ApplicationInit.class)) {
            obj.onApplicationInit(this);
        }
        state = State.INITIALIZED;

        for (ApplicationStart obj : factory.getObjectsByInterface(ApplicationStart.class)) {
            obj.onApplicationStart(this);
        }
        state = State.STARTED;
    }

    public void stop() throws ApplicationInitializationException {
        for (ApplicationStop obj : factory.getObjectsByInterface(ApplicationStop.class)) {
            obj.onApplicationStop(this);
        }
        state = State.STOPPED;
    }

}
