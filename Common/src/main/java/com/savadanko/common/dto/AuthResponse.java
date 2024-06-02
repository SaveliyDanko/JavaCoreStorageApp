package com.savadanko.common.dto;


import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
public class AuthResponse implements Serializable {
    private Map<String, CommandProperties> commandPropertiesMap;
    private final String message;

    public AuthResponse(String message) {
        this.message = message;
    }

    public AuthResponse(String message, Map<String, CommandProperties> commandPropertiesMap) {
        this.message = message;
        this.commandPropertiesMap = commandPropertiesMap;
    }

}
