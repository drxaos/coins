package com.github.drxaos.coins.javafx;

import java.util.Date;

public class TestApp extends WebApp {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected String getStartUrl() {
//        return "webapp:///index.html";
        return "http://localhost:4567/ui";
    }

    @Override
    protected void onRelocate(String url) {
        System.out.println("New URL: " + url);
    }

    @Override
    protected void onAlert(String data) {
        System.out.println("Alert: " + data);
    }

    @Override
    protected Object getAppBridge() {
        return new AppBridge();
    }

    public static class AppBridge {
        public String getUsername() {
            return System.getProperty("user.name");
        }

        public String getTime() {
            return new Date().toString();
        }

        public void println(String str) {
            System.out.println(str);
        }
    }
}
