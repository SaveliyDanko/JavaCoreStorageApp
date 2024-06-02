package com.savadanko.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class AuthDTO implements Serializable {
    private final String login;
    private final byte[] passwordHash;
}
