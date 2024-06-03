package com.savadanko.client.input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class FileInput implements IInput {
    public static LinkedList<String> commandStrings = new LinkedList<>();
    public static Set<String> filesSet = new HashSet<>();

    @Override
    public String getNextLine() {
        return commandStrings.pollFirst();
    }

    @Override
    public boolean hasNextLine() {
        return (!commandStrings.isEmpty());
    }

    public static void addFile(String filePath) throws IOException {
        String line;
        LinkedList<String> t = new LinkedList<>();
        try (FileReader reader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            while ((line = bufferedReader.readLine()) != null) {
                t.addLast(line);
            }
            commandStrings.addAll(0, t);
        }
    }

    public static void clear() {
        filesSet.clear();
    }
}