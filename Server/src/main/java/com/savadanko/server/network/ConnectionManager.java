package com.savadanko.server.network;

import com.savadanko.server.command.CommandFactory;
import com.savadanko.server.command.CommandRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionManager implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);

    private final ServerSocket serverSocket;

    private final ExecutorService acceptManager;

    private final BlockingQueue<CommandRequest> requests;

    private final Map<Socket, ObjectStreams> objectStreamMap;

    private final CommandFactory commandFactory;

    public ConnectionManager(BlockingQueue<CommandRequest> requests, Map<Socket, ObjectStreams> objectStreamMap, CommandFactory commandFactory) throws IOException {
        this.objectStreamMap = objectStreamMap;
        this.requests = requests;
        this.serverSocket = new ServerSocket(8888);
        this.acceptManager = Executors.newCachedThreadPool();

        this.commandFactory = commandFactory;
    }

    @Override
    public void run(){
        while (true){
            try{
                Socket socket = serverSocket.accept();
                objectStreamMap.put(socket, new ObjectStreams(new ObjectInputStream(socket.getInputStream()), new ObjectOutputStream(socket.getOutputStream())));
                acceptManager.execute(new ClientHandler(socket, objectStreamMap, requests, commandFactory));
            }
            catch (IOException e){
                log.error(e.getMessage());
            }
        }
    }
}
