package com.github.drxaos.coins.controller;

import com.github.drxaos.coins.domain.User;

public interface AbstractTransport<IN, OUT> {

    IN input(Class<IN> type);

    User loggedInUser();

    void auth(User user);

    String params(String name);

    void status(int httpCode);
}
