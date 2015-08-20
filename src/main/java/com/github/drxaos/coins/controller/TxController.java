package com.github.drxaos.coins.controller;


import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class TxController {
    public static void main(String[] args) {


        Spark.get("/hello", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                return "Hello World";
            }
        });
    }
}