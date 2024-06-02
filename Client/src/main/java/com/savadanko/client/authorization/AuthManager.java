package com.savadanko.client.authorization;

import com.savadanko.client.input.IInput;
import com.savadanko.common.dto.AuthDTO;
import com.savadanko.common.dto.AuthResponse;
import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;

public class AuthManager {
    private final IInput currentInput;
    @Getter
    private String login;
    private String password;

    private final ObjectInputStream in;
    private final ObjectOutputStream out;


    public AuthManager(IInput currentInput, ObjectOutputStream out, ObjectInputStream in){
        this.currentInput = currentInput;
        this.in = in;
        this.out = out;
    }

    public AuthResponse start() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        System.out.println("Input login: ");
        if (currentInput.hasNextLine()){
            this.login = currentInput.getNextLine();
        }

        System.out.println("Input password: ");
        if (currentInput.hasNextLine()){
            this.password = currentInput.getNextLine();
        }

        AuthDTO authDTO = new AuthDTO(login, PasswordHasher.hashPassword(password));

        out.writeObject(authDTO);
        out.flush();

        return (AuthResponse) in.readObject();
    }
}
