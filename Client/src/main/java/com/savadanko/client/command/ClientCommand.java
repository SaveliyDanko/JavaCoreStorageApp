package com.savadanko.client.command;

import com.savadanko.client.exceptions.ExecuteScriptException;

public interface ClientCommand {
    void execute() throws ExecuteScriptException;
}
