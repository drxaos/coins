package com.github.drxaos.coins.application;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public abstract class ApplicationProps implements ApplicationInit {

    protected Map<String, String> props = new HashMap<>();

    public String getString(String key, String defaultValue) {
        if (props.containsKey(key)) {
            return props.get(key);
        } else {
            return defaultValue;
        }
    }

    private static final List<String> TRUES = Arrays.asList("true", "yes", "1", "y", "on");

    public Boolean getBoolean(String key, Boolean defaultValue) {
        if (props.containsKey(key)) {
            return TRUES.contains(props.get(key).trim());
        } else {
            return defaultValue;
        }
    }

    public Integer getInteger(String key, Integer defaultValue) {
        if (props.containsKey(key)) {
            try {
                return Integer.parseInt(props.get(key));
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public Long getLong(String key, Long defaultValue) {
        if (props.containsKey(key)) {
            try {
                return Long.parseLong(props.get(key));
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public Float getFloat(String key, Float defaultValue) {
        if (props.containsKey(key)) {
            try {
                return Float.parseFloat(props.get(key));
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public Double getDouble(String key, Double defaultValue) {
        if (props.containsKey(key)) {
            try {
                return Double.parseDouble(props.get(key));
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    @Override
    abstract public void onApplicationInit(Application application) throws ApplicationInitializationException;
}
