package com.savadanko.client.input;

import java.util.Scanner;

public class UserInput implements IInput{
    private final Scanner scanner;

    public UserInput() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String getNextLine() {
        return scanner.nextLine();
    }

    @Override
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }
}
