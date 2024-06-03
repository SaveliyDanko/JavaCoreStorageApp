package com.savadanko.client;

import com.savadanko.client.connection.ConnectionManager;
import com.savadanko.client.input.CurrentInput;
import com.savadanko.client.input.FileInput;
import com.savadanko.client.input.UserInput;

public class ClientApp {
    public static void main(String[] args){
        if (args.length == 1){
            try{
                Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e){
                System.out.println("Port must be number value");
                System.exit(0);
            }
            new ClientManager(new ConnectionManager(),
                    new CurrentInput(
                            new UserInput(),
                            new FileInput()
                    )).start(Integer.parseInt(args[0]));
        }
        System.exit(0);
    }
}
