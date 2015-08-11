package com.github.drxaos.coins.application;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AutowiringFactory {

    private Map<String, Object> registry = new HashMap<>();
    private Set<Class> classRegistry = new HashSet<>();

    private class Target {
        Object obj;
        Field field;

        public Target(Object obj, Field field) {
            this.obj = obj;
            this.field = field;
        }

        public <T> void set(T instance) throws IllegalAccessException {
            field.setAccessible(true);
            field.set(obj, instance);
        }

        @Override
        public String toString() {
            return "Target{" +
                    "obj=" + obj +
                    ", field=" + field +
                    '}';
        }
    }

    private Map<String, List<Target>> targetsMap = new HashMap<>();

    public List<Object> getObjects() {
        return getObjectsByInterface(null);
    }

    public <T> List<T> getObjectsByInterface(Class<T> withInterface) {
        List<T> result = new ArrayList<>();
        for (Object o : registry.values()) {
            if (withInterface == null || withInterface.isAssignableFrom(o.getClass())) {
                result.add((T) o);
            }
        }
        return result;
    }

    public List<Object> getObjectsByAnnotation(Class<? extends Annotation> withAnnotation) {
        List<Object> result = new ArrayList<>();
        for (Object o : registry.values()) {
            if (withAnnotation == null || o.getClass().getAnnotation(withAnnotation) != null) {
                result.add(o);
            }
        }
        return result;
    }

    public List<Class> getClassesByAnnotation(Class<? extends Annotation> withAnnotation) {
        List<Class> result = new ArrayList<>();
        for (Class c : classRegistry) {
            if (withAnnotation == null || c.getAnnotation(withAnnotation) != null) {
                result.add(c);
            }
        }
        return result;
    }

    public <T> List<T> getClassesBySuperclass(Class<T> withSuperclass) {
        List<T> result = new ArrayList<>();
        for (Class c : classRegistry) {
            if (withSuperclass == null || withSuperclass.isAssignableFrom(c)) {
                result.add((T) c);
            }
        }
        return result;
    }

    public void registerClass(Class... classes) {
        for (Class aClass : classes) {
            registerClass(aClass);
        }
    }

    public void registerClass(Class cls) {
        classRegistry.add(cls);
    }

    public List<Object> createObject(Class... classes) throws FactoryException {
        List<Object> result = new ArrayList<>();
        for (Class aClass : classes) {
            result.add(createObject(aClass));
        }
        return result;
    }

    public <T> T createObject(Class<T> cls) throws FactoryException {

        String clsWiringName = getClsWiringName(cls);
        if (registry.containsKey(clsWiringName)) {
            throw new FactoryException("Object '" + clsWiringName + "' already in registry");
        }

        T instance;
        try {
            instance = cls.newInstance();
        } catch (Exception e) {
            throw new FactoryException("Cannot create new instance of " + cls, e);
        }

        registry.put(clsWiringName, instance);

        List<Target> targets = targetsMap.get(clsWiringName);
        if (targets != null) {
            for (Target target : targets) {
                try {
                    target.set(instance);
                } catch (IllegalAccessException e) {
                    throw new FactoryException("Cannot inject instance to " + target, e);
                }
            }
            targetsMap.remove(clsWiringName);
        }

        for (Field field : cls.getDeclaredFields()) {
            Autowire autowire = field.getDeclaredAnnotation(Autowire.class);
            if (autowire != null) {
                Target target = new Target(instance, field);
                if (registry.containsKey(autowire.value())) {
                    try {
                        target.set(registry.get(autowire.value()));
                    } catch (IllegalAccessException e) {
                        throw new FactoryException("Cannot inject instance to " + target, e);
                    }
                } else {
                    if (!targetsMap.containsKey(autowire.value())) {
                        targetsMap.put(autowire.value(), new ArrayList<>());
                    }
                    targetsMap.get(autowire.value()).add(target);
                }
            }
        }

        return instance;
    }

    public <T> String getClsWiringName(Class<T> cls) {
        Class c = cls;
        while (c != null) {
            try {
                Annotation clsAnnotation = c.getAnnotation(Component.class);
                if (clsAnnotation != null) {
                    Class<? extends Annotation> type = clsAnnotation.annotationType();
                    Method valueMethod = type.getDeclaredMethod("value");
                    Object value = valueMethod.invoke(clsAnnotation, (Object[]) null);
                    if (value != null && !value.toString().isEmpty()) {
                        return value.toString();
                    } else {
                        return c.getName();
                    }
                }
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                // no value
            }
            c = c.getSuperclass();
        }
        return cls.getName();
    }

    public <T> T findObject(String name) {
        return (T) registry.get(name);
    }

    public <T> T findObject(Class<T> cls) {
        return (T) registry.get(getClsWiringName(cls));
    }


    public Class[] getDependencies(Class cls) {
        try {
            Annotation clsAnnotation = cls.getAnnotation(Component.class);
            if (clsAnnotation != null) {
                Class<? extends Annotation> type = clsAnnotation.annotationType();
                Method dependenciesMethod = type.getDeclaredMethod("dependencies");
                return (Class[]) dependenciesMethod.invoke(clsAnnotation, (Object[]) null);
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            // no value
        }
        return new Class[0];
    }

    public <T> List<T> sortByDependencies(Collection<T> objs) {
        List<T> result = new ArrayList<>();
        List<String> resultNames = new ArrayList<>();
        HashSet<T> set = new HashSet<>(objs);

        int added = 1;
        while (added > 0) {
            added = 0;

            Iterator<T> i = set.iterator();
            while (i.hasNext()) {
                T o = i.next();
                Class[] deps = getDependencies(o.getClass());
                List<String> depsNames = new ArrayList<>();
                for (Class dep : deps) {
                    depsNames.add(getClsWiringName(dep));
                }
                if (deps.length == 0 || resultNames.containsAll(depsNames)) {
                    result.add(o);
                    resultNames.add(getClsWiringName(o.getClass()));
                    i.remove();
                    added++;
                }
            }
        }

        return result;
    }

}
