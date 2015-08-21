package com.github.drxaos.coins.spark.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

    public <T> T parse(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }
}