package com.savadanko.common.dto;


import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
public class AuthResponse implements Serializable {
    private Map<String, CommandProperties> commandPropertiesMap;
    private final String message;
    private final Status status;

    public AuthResponse(String message, Status status) {
        this.message = message;
        this.status = status;
    }

    public AuthResponse(String message, Map<String, CommandProperties> commandPropertiesMap, Status status) {
        this.message = message;
        this.commandPropertiesMap = commandPropertiesMap;
        this.status = status;
    }

}
