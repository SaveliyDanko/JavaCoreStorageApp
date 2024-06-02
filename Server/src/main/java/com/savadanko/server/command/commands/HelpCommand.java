package com.savadanko.server.command.commands;

import com.savadanko.common.models.Flat;
import com.savadanko.server.command.CommandResponse;
import com.savadanko.server.command.ECommand;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HelpCommand implements Command{
    private final String[] args;
    private final Flat flat;

    @Override
    public CommandResponse execute() {
        StringBuilder sb = new StringBuilder();
        for (ECommand item : ECommand.values()){
            sb.append(item.getName()).append(" - ").append(item.getDescription()).append("\n");
        }
        return new CommandResponse(sb.toString());
    }
}
