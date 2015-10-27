package com.github.drxaos.coins.application;

import com.github.drxaos.coins.application.events.ApplicationInitEventListener;
import com.github.drxaos.coins.application.events.ApplicationStartEventListener;
import com.github.drxaos.coins.application.events.ApplicationStopEventListener;
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
        for (ApplicationInitEventListener obj : factory.getObjectsByInterface(ApplicationInitEventListener.class)) {
            obj.onApplicationInit(this);
        }
        state = State.INITIALIZED;

        for (ApplicationStartEventListener obj : factory.getObjectsByInterface(ApplicationStartEventListener.class)) {
            obj.onApplicationStart(this);
        }
        state = State.STARTED;
    }

    public void stop() throws ApplicationInitializationException {
        for (ApplicationStopEventListener obj : factory.getObjectsByInterface(ApplicationStopEventListener.class)) {
            obj.onApplicationStop(this);
        }
        state = State.STOPPED;
    }

}
