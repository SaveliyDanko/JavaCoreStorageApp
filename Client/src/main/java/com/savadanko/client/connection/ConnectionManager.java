package com.savadanko.client.connection;

import com.savadanko.client.input.IInput;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Getter
@NoArgsConstructor
public class ConnectionManager {
    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void connect(String host, int port) throws IOException{
        this.socket = new Socket(host, port);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void disconnect() throws IOException{
        socket.close();
        out.close();
        in.close();
    }

    public void reconnect(IInput currentInput, String host, int port){
        System.out.println("Input 'exit' for exit or 'reconnect' for reconnect");
        if (currentInput.hasNextLine()){
            String command = currentInput.getNextLine();

            if (command.equals("exit")){
                System.exit(228);
            }
            try {
                this.socket = new Socket(host, port);
                this.out = new ObjectOutputStream(socket.getOutputStream());
                this.in = new ObjectInputStream(socket.getInputStream());
            }
            catch (IOException e){
                log.error(e.getMessage());
            }
        }
    }


}
