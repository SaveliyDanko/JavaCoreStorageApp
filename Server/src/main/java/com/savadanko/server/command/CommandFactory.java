package com.savadanko.server.command;

import com.savadanko.common.dto.CommandProperties;
import com.savadanko.server.command.commands.Command;
import com.savadanko.server.command.commands.HelpCommand;
import com.savadanko.server.command.commands.InsertCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private final Map<String, Class<? extends Command>> commandMap;
    private final Map<String, CommandProperties> commandPropertiesMap;


    public CommandFactory() {
        this.commandMap = new HashMap<>();
        this.commandPropertiesMap = new HashMap<>();

        commandMap.put(ECommand.HELP.getName(), HelpCommand.class);
        commandMap.put(ECommand.INSERT.getName(), InsertCommand.class);

        commandPropertiesMap.put(ECommand.HELP.getName(), new CommandProperties(0, false));
        commandPropertiesMap.put(ECommand.INSERT.getName(), new CommandProperties(0, true));
    }

    public Map<String, Class<? extends Command>> getCommandMap() {
        return commandMap;
    }

    public Map<String, CommandProperties> getCommandPropertiesMap() {
        return commandPropertiesMap;
    }
}
