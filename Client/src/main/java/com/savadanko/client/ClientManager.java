package com.savadanko.client;

import com.savadanko.client.authorization.AuthManager;
import com.savadanko.client.connection.ConnectionManager;
import com.savadanko.client.exceptions.InvalidAuthException;
import com.savadanko.client.exceptions.UnknownCommandException;
import com.savadanko.client.input.CurrentInput;
import com.savadanko.client.requestmanager.RequestBuilder;
import com.savadanko.common.dto.AuthResponse;
import com.savadanko.common.dto.Request;
import com.savadanko.common.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ClientManager {
    private static final Logger log = LoggerFactory.getLogger(ClientManager.class);

    private final CurrentInput currentInput;
    private final ConnectionManager connectionManager;
    private AuthResponse authResponse;
    private AuthManager authManager;

    public ClientManager(ConnectionManager connectionManager, CurrentInput currentInput) {
        this.connectionManager = connectionManager;
        this.currentInput = currentInput;
    }

    public void start() {
        String host = "localhost";
        int port = 8888;

        while (true){
            try {
                if (connectionManager.getSocket() == null){
                    connectionManager.connect(host, port);
                }
                if (connectionManager.getSocket() != null){
                    authManager = new AuthManager(currentInput,
                            connectionManager.getOut(),
                            connectionManager.getIn());

                    authResponse = authManager.start();
                }

                while (currentInput.hasNextLine()){
                    String command = currentInput.getNextLine();
                    try {
                        Request request = new RequestBuilder()
                                .build(currentInput,
                                        authResponse,
                                        command,
                                        authManager.getLogin());

                        connectionManager.getOut().writeObject(request);
                        connectionManager.getOut().flush();

                        Response response = (Response) connectionManager.getIn().readObject();
                        System.out.println(response.getMessage());
                    }
                    catch (UnknownCommandException e){
                        log.error(e.getMessage());
                        System.out.println(e.getMessage());
                    }
                }

                connectionManager.disconnect();
            }
            catch (IOException e){
                log.error(e.getMessage());
                connectionManager.reconnect(currentInput, host, port);
            }
            catch (ClassNotFoundException | NoSuchAlgorithmException | InvalidAuthException e){
                log.error(e.getMessage());
            }
        }
    }
}
