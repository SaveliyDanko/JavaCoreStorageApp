package com.savadanko.client.modelinput;

import com.savadanko.client.input.CurrentInput;
import com.savadanko.client.input.InputMode;
import com.savadanko.common.models.House;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HouseInputHelper {
    private static final Logger log = LoggerFactory.getLogger(CoordinatesInputHelper.class);
    private final CurrentInput currentInput;

    public HouseInputHelper(CurrentInput currentInput) {
        this.currentInput = currentInput;
    }

    public House getHouse() {
        System.out.println("Enter house details or '\\q' to quit:");

        String name = getName(); // Имя может быть null
        if (name != null && name.equals("\\q")) return null; // Отмена операции

        Long year = getYear();
        if (year == null) return null; // Отмена операции

        Long numberOfFloors = getNumberOfFloors();
        if (numberOfFloors == null) return null; // Отмена операции

        Integer numberOfFlatsOnFloor = getNumberOfFlatsOnFloor();
        if (numberOfFlatsOnFloor == null) return null; // Отмена операции

        Integer numberOfLifts = getNumberOfLifts();
        if (numberOfLifts == null) return null; // Отмена операции

        return new House(name, year, numberOfFloors, numberOfFlatsOnFloor, numberOfLifts);
    }

    private String getName() {
        System.out.print("Enter house name or leave empty (press Enter): ");
        if (currentInput.getInputMode().equals(InputMode.FILE_MODE) && !currentInput.hasNextLine()){
            currentInput.setInputMode(InputMode.USER_MODE);
        }
        String input = currentInput.getNextLine().trim();
        if (input.equals("\\q")) {
            System.out.println("Operation cancelled by user.");
            return "\\q"; // Special return to indicate cancellation
        }
        return input.isEmpty() ? null : input;
    }

    private Long getYear() {
        while (true) {
            System.out.print("Enter the year (must be greater than 0): ");
            if (currentInput.getInputMode().equals(InputMode.FILE_MODE) && !currentInput.hasNextLine()){
                currentInput.setInputMode(InputMode.USER_MODE);
            }
            String input = currentInput.getNextLine().trim();
            if (input.equals("\\q")) {
                System.out.println("Operation cancelled by user.");
                return null;
            }
            try {
                long year = Long.parseLong(input);
                if (year > 0) {
                    return year;
                } else {
                    System.out.println("Error: Year must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                log.error(e.getMessage());
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private Long getNumberOfFloors() {
        return getPositiveLong();
    }

    private Integer getNumberOfFlatsOnFloor() {
        return getPositiveInteger("Enter the number of flats on each floor (must be greater than 0): ");
    }

    private Integer getNumberOfLifts() {
        return getPositiveInteger("Enter the number of lifts (must be greater than 0): ");
    }

    private Long getPositiveLong() {
        while (true) {
            System.out.print("Enter the number of floors (must be greater than 0): ");
            if (currentInput.getInputMode().equals(InputMode.FILE_MODE) && !currentInput.hasNextLine()){
                currentInput.setInputMode(InputMode.USER_MODE);
            }
            String input = currentInput.getNextLine().trim();
            if (input.equals("\\q")) {
                System.out.println("Operation cancelled by user.");
                return null;
            }
            try {
                long value = Long.parseLong(input);
                if (value > 0) {
                    return value;
                } else {
                    System.out.println("Error: Value must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                log.error(e.getMessage());
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private Integer getPositiveInteger(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (currentInput.getInputMode().equals(InputMode.FILE_MODE) && !currentInput.hasNextLine()){
                currentInput.setInputMode(InputMode.USER_MODE);
            }
            String input = currentInput.getNextLine().trim();
            if (input.equals("\\q")) {
                System.out.println("Operation cancelled by user.");
                return null;
            }
            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                } else {
                    System.out.println("Error: Value must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                log.error(e.getMessage());
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
}
