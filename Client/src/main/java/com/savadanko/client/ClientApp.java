package com.savadanko.client;

import com.savadanko.client.connection.ConnectionManager;
import com.savadanko.client.input.CurrentInput;
import com.savadanko.client.input.FileInput;
import com.savadanko.client.input.UserInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientApp {
    private static final Logger logger = LoggerFactory.getLogger(ClientApp.class);

    public static void main(String[] args){

        new ClientManager(new ConnectionManager(),
                new CurrentInput(
                        new UserInput(),
                        new FileInput()
                )).start();
    }
}
