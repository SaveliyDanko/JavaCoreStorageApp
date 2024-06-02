package com.savadanko.server.command;

import com.savadanko.common.models.Flat;
import com.savadanko.server.command.commands.Command;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;

public class CommandExecutor implements Runnable{
    private final BlockingQueue<CommandRequest> requests;
    private final BlockingQueue<CommandResponse> responses;

    private final CommandFactory commandFactory;

    public CommandExecutor(BlockingQueue<CommandRequest> requests,
                           BlockingQueue<CommandResponse> responses,
                           CommandFactory commandFactory) {

        this.requests = requests;
        this.responses = responses;

        this.commandFactory = commandFactory;
    }

    @Override
    public void run() {
        try {
            while (true) {
                CommandRequest commandRequest = requests.take();
                Class<?> commandClass = commandFactory.getCommandMap().get(commandRequest.getRequest().getCommand());

                if (commandClass != null) {
                    Object object = commandClass
                            .getDeclaredConstructor(String[].class, Flat.class)
                            .newInstance(commandRequest.getRequest().getArgs(), commandRequest.getRequest().getFlat());

                    Command command = (Command) object;

                    CommandResponse commandResponse = new CommandResponse(
                            commandRequest.getSocket(),
                            command.execute().getMessage());

                    responses.put(commandResponse);
                } else {
                    System.err.println("Command not found: " + commandRequest.getRequest().getCommand());

                    CommandResponse commandResponse = new CommandResponse(
                            commandRequest.getSocket(),
                            "Command not found: " + commandRequest.getRequest().getCommand());

                    responses.put(commandResponse);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("CommandExecutor interrupted");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            System.err.println("Error instantiating command: " + e.getMessage());
        }
    }
}
