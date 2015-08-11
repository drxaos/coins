package com.github.drxaos.coins.controller;


import spark.Spark;

public class TxController {
    public static void main(String[] args) {



        Spark.get("/hello", (req, res) -> "Hello World");
    }
}