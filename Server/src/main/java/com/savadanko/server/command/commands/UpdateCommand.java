package com.savadanko.server.command.commands;

import com.savadanko.common.models.Flat;
import com.savadanko.server.command.CommandResponse;
import com.savadanko.server.database.DataBaseHandler;
import com.savadanko.server.database.sql.Tables;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateCommand implements Command{
    private final String[] args;
    private final Flat flat;
    private final String userLogin;

    @Override
    public CommandResponse execute() {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance();
        for (Object o : dataBaseHandler.readAll(Tables.FLATS).values()){
            Flat f = (Flat) o;
            if (f.getId() == Long.parseLong(args[0])){
                if (f.getOwner().equals(userLogin)){
                    dataBaseHandler.update(Long.parseLong(args[0]), flat, Tables.FLATS);
                    return new CommandResponse("Update object successfully");
                }
                else return new CommandResponse("You can't update an object, it doesn't belong to you");
            }
        }
        return new CommandResponse("There is no object with this ID");
    }
}
