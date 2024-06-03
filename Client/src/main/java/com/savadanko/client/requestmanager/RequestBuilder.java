package com.savadanko.client.requestmanager;

import com.savadanko.client.exceptions.UnknownCommandException;
import com.savadanko.client.input.CurrentInput;
import com.savadanko.client.modelinput.FlatInputHelper;
import com.savadanko.common.dto.AuthResponse;
import com.savadanko.common.dto.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class RequestBuilder {
    private static final Logger log = LoggerFactory.getLogger(RequestBuilder.class);

    public Request build(CurrentInput currentInput, AuthResponse authResponse, String command, String owner) throws UnknownCommandException {
        String commandName = command.split(" ")[0];
        String[] commandArgs = Arrays.copyOfRange(command.split(" "), 1, command.split(" ").length);

        if (authResponse.getCommandPropertiesMap().containsKey(commandName)) {
            int expectedArgsCount = authResponse.getCommandPropertiesMap().get(commandName).getArgsCount();
            boolean requiresObject = authResponse.getCommandPropertiesMap().get(commandName).isObject();

            if (commandArgs.length != expectedArgsCount) {
                log.error("Invalid arguments count for command: {}. Expected: {}, Provided: {}", commandName, expectedArgsCount, commandArgs.length);
                throw new IllegalArgumentException("Illegal arguments count");
            }

            if (requiresObject) {
                return new Request(
                        commandName,
                        commandArgs,
                        new FlatInputHelper(currentInput, owner).getFlat(),
                        owner);
            } else {
                return new Request(
                        commandName,
                        commandArgs,
                        null,
                        owner);
            }
        } else {
            log.error("Unknown command: {}", commandName);
            throw new UnknownCommandException("Unknown command: " + command);
        }
    }
}
