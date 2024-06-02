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

public class ResponseManager implements Runnable{


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
            while (true){
                CommandResponse commandResponse = commandResponses.take();
                Response response = new Response(commandResponse.getMessage());

                ObjectOutputStream out = objectStreamMap.get(commandResponse.getSocket()).getOut();
                out.writeObject(response);
                out.flush();
            }
        }
        catch (IOException | InterruptedException e){
            log.error(e.getMessage());
        }
    }
}
