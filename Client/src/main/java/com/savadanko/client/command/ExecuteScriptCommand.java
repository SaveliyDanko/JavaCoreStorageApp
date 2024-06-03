package com.savadanko.client.command;

import com.savadanko.client.exceptions.ExecuteScriptException;
import com.savadanko.client.input.CurrentInput;
import com.savadanko.client.input.FileInput;
import com.savadanko.client.input.InputMode;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class ExecuteScriptCommand implements ClientCommand {
    private final CurrentInput currentInput;
    private final String command;

    public ExecuteScriptCommand(CurrentInput currentInput, String command) {
        this.currentInput = currentInput;
        this.command = command;
    }

    @Override
    public void execute() throws ExecuteScriptException{
        if (command.split(" ").length == 2){
            String fileName = command.split(" ")[1];
            String classLocation = ExecuteScriptCommand.class.getProtectionDomain().getCodeSource().getLocation().toString();
            classLocation = classLocation.substring(5);
            LinkedList<String> array = new LinkedList<>(List.of(classLocation.split(Pattern.quote(File.separator))));
            array.removeLast();
            classLocation = String.join(File.separator, array);
            String path = classLocation + File.separator + fileName;

            try {
                if (FileInput.filesSet.add(path)){
                    FileInput.addFile(path);
                    currentInput.setInputMode(InputMode.FILE_MODE);
                }
            }
            catch (IOException e){
                throw new ExecuteScriptException(e.getMessage());
            }
        }
    }
}
