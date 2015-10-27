package com.github.drxaos.coins.application.config;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationInitEventListener;
import com.github.drxaos.coins.application.factory.Component;
import com.google.common.base.Optional;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public abstract class ApplicationProps implements ApplicationInitEventListener {

    protected Map<String, String> props = new HashMap<>();

    public String getString(String key, String defaultValue) {
        return Optional.fromNullable(props.get(key)).or(defaultValue);
    }

    private static final List<String> TRUES = Arrays.asList("true", "yes", "1", "y", "on");

    <T, R> R consumeExceptions(T val, Function<T, R> func) {
        try {
            return func.apply(val);
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return Optional.fromNullable(props.get(key)).transform(input -> TRUES.contains(input.trim())).or(defaultValue);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        return Optional.fromNullable(consumeExceptions(props.get(key), Ints::tryParse)).or(defaultValue);
    }

    public Long getLong(String key, Long defaultValue) {
        return Optional.fromNullable(consumeExceptions(props.get(key), Longs::tryParse)).or(defaultValue);
    }

    public Float getFloat(String key, Float defaultValue) {
        return Optional.fromNullable(consumeExceptions(props.get(key), Floats::tryParse)).or(defaultValue);
    }

    public Double getDouble(String key, Double defaultValue) {
        return Optional.fromNullable(consumeExceptions(props.get(key), Doubles::tryParse)).or(defaultValue);
    }

    @Override
    abstract public void onApplicationInit(Application application) throws ApplicationInitializationException;
}
