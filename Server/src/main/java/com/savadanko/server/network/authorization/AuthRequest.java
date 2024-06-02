package com.savadanko.server.network.authorization;

public class AuthRequest {
    private final String message;

    public AuthRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
