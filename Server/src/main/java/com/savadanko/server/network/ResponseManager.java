package com.savadanko.server.network;

import com.savadanko.common.dto.Response;
import com.savadanko.server.command.CommandResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class ResponseManager implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ResponseManager.class);
    private final BlockingQueue<CommandResponse> commandResponses;
    private final Map<Socket, ObjectStreams> objectStreamMap;

    public ResponseManager(BlockingQueue<CommandResponse> commandResponses, Map<Socket, ObjectStreams> objectStreamMap) {
        this.commandResponses = commandResponses;
        this.objectStreamMap = objectStreamMap;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                CommandResponse commandResponse = commandResponses.take();
                Response response = new Response(commandResponse.getMessage());

                Socket socket = commandResponse.getSocket();
                ObjectOutputStream out = objectStreamMap.get(socket).getOut();

                try {
                    out.writeObject(response);
                    out.flush();
                } catch (IOException e) {
                    log.error("Error sending response to client: {}", e.getMessage(), e);
                    closeSocket(socket);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("ResponseManager was interrupted and is stopping.");
        } catch (Exception e) {
            log.error("Unexpected error in ResponseManager: {}", e.getMessage(), e);
        } finally {
            cleanUp();
        }
    }

    private void closeSocket(Socket socket) {
        try {
            if (!socket.isClosed()) {
                socket.close();
                objectStreamMap.remove(socket);
                log.info("Socket closed and removed from map.");
            }
        } catch (IOException e) {
            log.error("Error closing socket: {}", e.getMessage(), e);
        }
    }

    private void cleanUp() {
        for (Socket socket : objectStreamMap.keySet()) {
            closeSocket(socket);
        }
        log.info("ResponseManager cleanup completed.");
    }
}
