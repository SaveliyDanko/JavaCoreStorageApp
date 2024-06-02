package com.savadanko.server.command.commands;

import com.savadanko.common.models.Flat;
import com.savadanko.server.command.CommandResponse;
import com.savadanko.server.database.DataBaseHandler;
import com.savadanko.server.database.sql.Tables;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeleteCommand implements Command{
    private final String[] args;
    private final Flat flat;
    private final String ownerLogin;

    @Override
    public CommandResponse execute() {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance();
        Flat f = (Flat) dataBaseHandler.read(Long.parseLong(args[0]), Tables.FLATS);
        if (f != null){
            if (f.getOwner().equals(ownerLogin)){
                dataBaseHandler.delete(Long.parseLong(args[0]), Tables.FLATS);
                return new CommandResponse("Delete flat successfully");
            }
            else return new CommandResponse("You can't delete an object, it doesn't belong to you");
        }
        else return new CommandResponse("There is no object with this ID");
    }
}
