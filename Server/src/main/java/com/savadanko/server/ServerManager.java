package com.savadanko.server;

import com.savadanko.server.command.CommandExecutor;
import com.savadanko.server.command.CommandFactory;
import com.savadanko.server.command.CommandRequest;
import com.savadanko.server.command.CommandResponse;
import com.savadanko.server.network.ConnectionManager;
import com.savadanko.server.network.ObjectStreams;
import com.savadanko.server.network.ResponseManager;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerManager {
    private final BlockingQueue<CommandRequest> requests;
    private final BlockingQueue<CommandResponse> responses;
    private final Map<Socket, ObjectStreams> objectStreamMap;

    ExecutorService commandExecutorService;
    ExecutorService responseExecutorService;

    ConnectionManager connectionManager;

    CommandFactory commandFactory;

    public ServerManager() throws IOException {
        this.requests = new LinkedBlockingQueue<>();
        this.responses = new LinkedBlockingQueue<>();
        this.objectStreamMap = new HashMap<>();

        this.commandExecutorService = Executors.newFixedThreadPool(10);
        this.responseExecutorService = Executors.newFixedThreadPool(10);

        this.commandFactory = new CommandFactory();

        this.connectionManager = new ConnectionManager(requests, objectStreamMap, commandFactory);
    }

    public void start(){
        new Thread(connectionManager).start();
        commandExecutorService.execute(new CommandExecutor(requests, responses, commandFactory));
        responseExecutorService.execute(new ResponseManager(responses, objectStreamMap));
    }
}
