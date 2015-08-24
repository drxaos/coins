package com.github.drxaos.coins.application.factory;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AutowiringFactory {

    private Map<String, Object> registry = new HashMap<>();
    private List<Object> sortedRegistryCache;

    private Set<Class> classRegistry = new HashSet<>();

    @ToString
    @EqualsAndHashCode
    private class Target {
        Object obj;
        Field field;

        public Target(Object obj, Field field) {
            this.obj = obj;
            this.field = field;
        }

        public <T> T get() throws IllegalAccessException {
            field.setAccessible(true);
            return (T) field.get(obj);
        }

        public <T> void set(T instance) throws IllegalAccessException {
            field.setAccessible(true);
            field.set(obj, instance);
        }
    }

    private Map<String, List<Target>> targetsMap = new HashMap<>();

    public List<Object> getObjects() {
        return getObjectsByInterface(null);
    }

    public <T> List<T> getObjectsByInterface(Class<T> withInterface) {
        cacheSortedRegistry();
        return FluentIterable.from(sortedRegistryCache)
                .filter((o) -> withInterface == null || withInterface.isAssignableFrom(o.getClass()))
                .transform((o) -> (T) o).toList();
    }

    public void cacheSortedRegistry() {
        sortedRegistryCache = Optional.fromNullable(sortedRegistryCache)
                .or(() -> sortByDependencies(registry.values()));
    }

    public List<Object> getObjectsByAnnotation(Class<? extends Annotation> withAnnotation) {
        cacheSortedRegistry();
        return FluentIterable.from(sortedRegistryCache)
                .filter((o) -> withAnnotation == null || o.getClass().getAnnotation(withAnnotation) != null)
                .toList();
    }

    public List<Class> getClassesByAnnotation(Class<? extends Annotation> withAnnotation) {
        return FluentIterable.from(classRegistry)
                .filter((c) -> withAnnotation == null || c.getAnnotation(withAnnotation) != null)
                .toList();
    }

    public <T> List<Class<T>> getClassesBySuperclass(Class<T> withSuperclass) {
        return FluentIterable.from(classRegistry)
                .filter((c) -> withSuperclass == null || withSuperclass.isAssignableFrom(c))
                .transform((c) -> (Class<T>) c).toList();
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
        return FluentIterable.of(classes).<Object>transform((c) -> createObject(c)).toList();
    }

    public <T> T createObject(Class<T> cls) throws FactoryException {

        String clsWiringName = getClsWiringName(cls);
        if (registry.containsKey(clsWiringName)) {
            throw new FactoryException("Object '" + clsWiringName + "' already in registry");
        }

        T instance;
        try {
            Constructor<T> constructor = cls.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
        } catch (Exception e) {
            throw new FactoryException("Cannot create new instance of " + cls, e);
        }

        registry.put(clsWiringName, instance);
        sortedRegistryCache = null;

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

        autowire(instance, true);

        return instance;
    }

    public <T> T autowire(T instance) {
        return autowire(instance, false);
    }

    private <T> T autowire(T instance, boolean registerTargets) {
        Class<T> cls = (Class<T>) instance.getClass();
        Class c = cls;
        while (c != null) {
            for (Field field : c.getDeclaredFields()) {
                {
                    Inject inject = (Inject) Iterables.tryFind(
                            Arrays.asList(field.getDeclaredAnnotations()),
                            (a) -> (a instanceof Inject)).orNull();

                    if (inject != null) {
                        String autowireName = inject.value();
                        if (autowireName == null || autowireName.isEmpty()) {
                            autowireName = getClsWiringName(field.getType());
                        }
                        Target target = new Target(instance, field);
                        if (registry.containsKey(autowireName)) {
                            try {
                                target.set(registry.get(autowireName));
                            } catch (IllegalAccessException e) {
                                throw new FactoryException("Cannot inject instance to " + target, e);
                            }
                        } else if (registerTargets) {
                            if (!targetsMap.containsKey(autowireName)) {
                                targetsMap.put(autowireName, new ArrayList());
                            }
                            targetsMap.get(autowireName).add(target);
                        }
                    }
                }
                {
                    Autowire autowire = (Autowire) Iterables.tryFind(
                            Arrays.asList(field.getDeclaredAnnotations()),
                            (a) -> (a instanceof Autowire)).orNull();

                    if (autowire != null) {
                        Target target = new Target(instance, field);
                        try {
                            autowire(target.get());
                        } catch (IllegalAccessException e) {
                            throw new FactoryException("Cannot autowire instance " + target, e);
                        }
                    }
                }
            }
            c = c.getSuperclass();
        }
        return instance;
    }

    private Set<String> autowiredFieldsNames(Class cls) {
        Set<String> result = new HashSet<>();
        Class c = cls;
        while (c != null) {
            for (Field field : cls.getDeclaredFields()) {
                Inject inject = (Inject) Iterables.tryFind(
                        Arrays.asList(field.getDeclaredAnnotations()),
                        (a) -> (a instanceof Inject)).orNull();

                if (inject != null) {
                    result.add(Optional
                            .fromNullable(Strings.emptyToNull(inject.value()))
                            .or(getClsWiringName(field.getType())));
                }
            }
            c = c.getSuperclass();
        }
        return result;
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
                depsNames.addAll(autowiredFieldsNames(o.getClass()));
                if (depsNames.isEmpty() || resultNames.containsAll(depsNames)) {
                    result.add(o);
                    resultNames.add(getClsWiringName(o.getClass()));
                    i.remove();
                    added++;
                }
            }
        }

        if (!set.isEmpty()) {
            throw new FactoryException("Cannot find dependencies:\n" +
                    Joiner.on("\n").join(Collections2.transform(set, (o) ->
                            o.getClass().getName() + ": " + FluentIterable.of(getDependencies(o.getClass()))
                                    .transform(this::getClsWiringName)
                                    .append(autowiredFieldsNames(o.getClass()))
                                    .filter(resultNames::contains))));
        }

        return result;
    }

    public <T> T newInstance(Class<T> cls) {
        try {
            T o = cls.newInstance();
            autowire(o);
            return o;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new FactoryException("Cannot create new instance of " + cls, e);
        }
    }

}
