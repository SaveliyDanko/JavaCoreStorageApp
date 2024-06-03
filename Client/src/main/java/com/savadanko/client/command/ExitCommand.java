package com.savadanko.client.command;

import com.savadanko.client.connection.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExitCommand implements ClientCommand{
    private static final Logger log = LoggerFactory.getLogger(ExitCommand.class);
    private final ConnectionManager connectionManager;

    public ExitCommand(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void execute() {
        log.info("Exiting application");
        connectionManager.disconnect();
        System.out.println("Client disconnected.");
    }
}
