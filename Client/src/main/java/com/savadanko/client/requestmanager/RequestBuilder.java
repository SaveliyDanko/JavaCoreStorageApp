package com.savadanko.client.requestmanager;

import com.savadanko.client.exceptions.UnknownCommandException;
import com.savadanko.client.input.CurrentInput;
import com.savadanko.client.modelinput.FlatInputHelper;
import com.savadanko.common.dto.AuthResponse;
import com.savadanko.common.dto.Request;

import java.util.Arrays;

public class RequestBuilder {

    public Request build(CurrentInput currentInput, AuthResponse authResponse, String command, String owner) throws UnknownCommandException{
        String commandName = command.split(" ")[0];
        String[] commandArgs = Arrays.copyOfRange(command.split(" "), 1, command.split(" ").length);
        if (authResponse.getCommandPropertiesMap().containsKey(commandName)){
            if (commandArgs.length != authResponse.getCommandPropertiesMap().get(commandName).getArgsCount()){
                throw new IllegalArgumentException("Illegal arguments count");
            }
            if (authResponse.getCommandPropertiesMap().get(commandName).isObject()){
                return new Request(commandName, commandArgs, new FlatInputHelper(currentInput, owner).getFlat());
            }
            else return new Request(commandName, commandArgs, null);
        }
        else throw new UnknownCommandException("Unknown command: " + command);
    }
}
