package com.github.drxaos.coins.application.config;

import com.github.drxaos.coins.application.Application;
import com.github.drxaos.coins.application.ApplicationInitializationException;
import com.github.drxaos.coins.application.events.ApplicationInit;
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

@Component
public abstract class ApplicationProps implements ApplicationInit {

    protected Map<String, String> props = new HashMap<>();

    public String getString(String key, String defaultValue) {
        return Optional.fromNullable(props.get(key)).or(defaultValue);
    }

    private static final List<String> TRUES = Arrays.asList("true", "yes", "1", "y", "on");

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return Optional.fromNullable(props.get(key)).transform(input -> TRUES.contains(input.trim())).or(defaultValue);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        return Optional.fromNullable(Ints.tryParse(props.get(key))).or(defaultValue);
    }

    public Long getLong(String key, Long defaultValue) {
        return Optional.fromNullable(Longs.tryParse(props.get(key))).or(defaultValue);
    }

    public Float getFloat(String key, Float defaultValue) {
        return Optional.fromNullable(Floats.tryParse(props.get(key))).or(defaultValue);
    }

    public Double getDouble(String key, Double defaultValue) {
        return Optional.fromNullable(Doubles.tryParse(props.get(key))).or(defaultValue);
    }

    @Override
    abstract public void onApplicationInit(Application application) throws ApplicationInitializationException;
}
