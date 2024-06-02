package com.savadanko.server.command;

import com.savadanko.common.dto.CommandProperties;
import com.savadanko.server.command.commands.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CommandFactory {
    private final Map<String, Class<? extends Command>> commandMap;
    private final Map<String, CommandProperties> commandPropertiesMap;


    public CommandFactory() {
        this.commandMap = new HashMap<>();
        this.commandPropertiesMap = new HashMap<>();

        commandMap.put(ECommand.HELP.getName(), HelpCommand.class);
        commandMap.put(ECommand.INSERT.getName(), InsertCommand.class);
        commandMap.put(ECommand.REMOVE_KEY.getName(), DeleteCommand.class);
        commandMap.put(ECommand.SHOW.getName(), ShowCommand.class);
        commandMap.put(ECommand.INFO.getName(), InfoCommand.class);
        commandMap.put(ECommand.UPDATE.getName(), UpdateCommand.class);
        commandMap.put(ECommand.CLEAR.getName(), ClearCommand.class);

        commandPropertiesMap.put(ECommand.HELP.getName(), new CommandProperties(0, false));
        commandPropertiesMap.put(ECommand.INSERT.getName(), new CommandProperties(0, true));
        commandPropertiesMap.put(ECommand.REMOVE_KEY.getName(), new CommandProperties(1, false));
        commandPropertiesMap.put(ECommand.SHOW.getName(), new CommandProperties(0, false));
        commandPropertiesMap.put(ECommand.INFO.getName(), new CommandProperties(0, false));
        commandPropertiesMap.put(ECommand.UPDATE.getName(), new CommandProperties(1, true));
        commandPropertiesMap.put(ECommand.CLEAR.getName(), new CommandProperties(0, false));
    }

}
