package com.savadanko.common.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private long id;
    private final String login;
    private final String password;

    public User(String password, String login) {
        this.password = password;
        this.login = login;
    }
}
