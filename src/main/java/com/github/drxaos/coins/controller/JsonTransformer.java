package com.github.drxaos.coins.controller;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

    public <T> T parse(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }
}