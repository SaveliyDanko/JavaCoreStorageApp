package com.savadanko.server.command;

import java.net.Socket;

public class CommandResponse {
    private Socket socket;
    private final String message;

    public CommandResponse(String message) {
        this.message = message;
    }

    public CommandResponse(Socket socket, String message) {
        this.socket = socket;
        this.message = message;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getMessage() {
        return message;
    }
}
