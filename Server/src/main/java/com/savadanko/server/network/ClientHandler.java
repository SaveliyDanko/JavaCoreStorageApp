package com.savadanko.server.network;

import com.savadanko.common.dto.AuthResponse;
import com.savadanko.common.dto.Request;
import com.savadanko.common.dto.Response;
import com.savadanko.common.dto.Status;
import com.savadanko.server.command.CommandFactory;
import com.savadanko.server.command.CommandRequest;
import com.savadanko.server.network.authorization.AuthManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class ClientHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final CommandFactory commandFactory;
    BlockingQueue<CommandRequest> requests;
    AuthManager authManager;

    public ClientHandler(Socket socket, Map<Socket, ObjectStreams> objectStreamMap, BlockingQueue<CommandRequest> requests, CommandFactory commandFactory) throws IOException {
        this.socket = socket;
        this.authManager = new AuthManager();
        this.requests = requests;

        this.in = objectStreamMap.get(socket).getIn();
        this.out = objectStreamMap.get(socket).getOut();
        this.commandFactory = commandFactory;
    }

    @Override
    public void run() {
        try {
            while (true){
                AuthResponse response = authManager.authorization(in, commandFactory);
                if (response.getStatus().equals(Status.STATUS_200)){
                    out.writeObject(response);
                    out.flush();
                    break;
                }
                out.writeObject(response);
                out.flush();
            }
            while (true){
                Request request = (Request) in.readObject();
                CommandRequest commandRequest = new CommandRequest(socket, request);
                requests.put(commandRequest);
            }
        }
        catch (IOException | ClassNotFoundException | InterruptedException e){
            log.error(e.getMessage());
        }
    }
}
