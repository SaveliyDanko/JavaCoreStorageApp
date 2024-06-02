package com.savadanko.server.network.authorization;

import com.savadanko.common.dto.AuthDTO;
import com.savadanko.common.dto.AuthResponse;
import com.savadanko.common.models.User;
import com.savadanko.server.command.CommandFactory;
import com.savadanko.server.database.DataBaseHandler;
import com.savadanko.server.database.sql.Tables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;

public class AuthManager {
    private static final Logger log = LoggerFactory.getLogger(AuthManager.class);
    private final DataBaseHandler dataBaseHandler;

    public AuthManager() {
        dataBaseHandler = DataBaseHandler.getInstance();
    }

    public AuthResponse authorization(ObjectInputStream ois, CommandFactory commandFactory){
        User user;
        try {
            AuthDTO authDTO = (AuthDTO) ois.readObject();
            for (Object o : dataBaseHandler.readAll(Tables.USERS).values()){
                user = (User) o;
                if (user.getLogin().equals(authDTO.getLogin())){
                    if (Arrays.equals(user.getPasswordHash(), authDTO.getPasswordHash())){
                        return new AuthResponse("Valid auth", commandFactory.getCommandPropertiesMap());
                    }
                    else return new AuthResponse("Invalid auth");
                }
            }
            register(authDTO.getLogin(), authDTO.getPasswordHash());
            return new AuthResponse("Registration", commandFactory.getCommandPropertiesMap());
        }
        catch (IOException | ClassNotFoundException e){
            log.error(e.getMessage());
        }
        return null;
    }

    public void register(String login, byte[] passwordHash){
        User user = new User(login, passwordHash);
        dataBaseHandler.create(user, Tables.USERS);
        System.out.println("Add user " + user);
    }
}
