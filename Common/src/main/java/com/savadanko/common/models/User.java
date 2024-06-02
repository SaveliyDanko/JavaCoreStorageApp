package com.savadanko.common.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class User implements Serializable {
    private long id;
    private final String login;
    private final byte[] passwordHash;

    public User(String login, byte[] passwordHash) {
        this.login = login;
        this.passwordHash = passwordHash;
    }
}
