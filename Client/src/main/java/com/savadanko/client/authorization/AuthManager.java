package com.savadanko.client.authorization;

import com.savadanko.client.exceptions.InvalidAuthException;
import com.savadanko.client.input.IInput;
import com.savadanko.common.dto.AuthDTO;
import com.savadanko.common.dto.AuthResponse;
import com.savadanko.common.dto.Status;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;

public class AuthManager {
    private static final Logger log = LoggerFactory.getLogger(AuthManager.class);

    private final IInput currentInput;
    @Getter
    private String login;
    private String password;

    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public AuthManager(IInput currentInput, ObjectOutputStream out, ObjectInputStream in) {
        this.currentInput = currentInput;
        this.in = in;
        this.out = out;
    }

    public AuthResponse start() throws NoSuchAlgorithmException, IOException, ClassNotFoundException, InvalidAuthException {
        while (true) {
            readCredentials();
            if (login == null || password == null) {
                log.warn("Login or password was not provided. Retrying...");
                continue;
            }

            byte[] hashedPassword = PasswordHasher.hashPassword(password, null); // Используем null, чтобы указать, что соль не используется на стороне клиента
            AuthDTO authDTO = new AuthDTO(login, hashedPassword, null); // Соль здесь не нужна
            sendAuthRequest(authDTO);
            AuthResponse authResponse = receiveAuthResponse();
            if (authResponse.getStatus().equals(Status.STATUS_200)) {
                log.info("Authentication successful for user: {}", login);
                return authResponse;
            } else {
                log.warn("Invalid authentication attempt for user: {}", login);
                System.out.println("Invalid auth, incorrect password, try again");
            }
        }
    }

    private void readCredentials() {
        System.out.println("Input login: ");
        if (currentInput.hasNextLine()) {
            this.login = currentInput.getNextLine().trim();
            if (this.login.isEmpty()) {
                this.login = null;
            }
        }

        System.out.println("Input password: ");
        if (currentInput.hasNextLine()) {
            this.password = currentInput.getNextLine().trim();
            if (this.password.isEmpty()) {
                this.password = null;
            }
        }
    }

    private void sendAuthRequest(AuthDTO authDTO) throws IOException {
        out.writeObject(authDTO);
        out.flush();
    }

    private AuthResponse receiveAuthResponse() throws IOException, ClassNotFoundException {
        return (AuthResponse) in.readObject();
    }
}
