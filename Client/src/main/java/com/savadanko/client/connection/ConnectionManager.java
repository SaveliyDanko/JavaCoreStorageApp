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

    public void connect(String host, int port) throws IOException {
        try {
            this.socket = new Socket(host, port);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            log.info("Connected to server at {}:{}", host, port);
        } catch (IOException e) {
            log.error("Failed to connect to server at {}:{} - {}", host, port, e.getMessage());
            throw e;
        }
    }

    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                log.info("Socket closed");
            }
            if (out != null) {
                out.close();
                log.info("Output stream closed");
            }
            if (in != null) {
                in.close();
                log.info("Input stream closed");
            }
        } catch (IOException e) {
            log.error("Failed to close connection - {}", e.getMessage());
        }
    }

    public void reconnect(IInput currentInput, String host, int port) {
        System.out.println("Input 'exit' to exit or 'reconnect' to reconnect:");
        if (currentInput.hasNextLine()) {
            String command = currentInput.getNextLine();
            if ("exit".equalsIgnoreCase(command)) {
                log.info("Exiting application");
                System.exit(0);
            } else if ("reconnect".equalsIgnoreCase(command)) {
                try {
                    disconnect();
                    connect(host, port);
                } catch (IOException e) {
                    log.error("Failed to reconnect to server at {}:{} - {}", host, port, e.getMessage());
                }
            } else {
                log.warn("Unknown command: {}", command);
                System.out.println("Unknown command. Please input 'exit' or 'reconnect'.");
            }
        }
    }
}
