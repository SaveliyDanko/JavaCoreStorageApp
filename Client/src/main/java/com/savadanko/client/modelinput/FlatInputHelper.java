package com.savadanko.client.modelinput;

import com.savadanko.client.input.CurrentInput;
import com.savadanko.client.input.InputMode;
import com.savadanko.common.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlatInputHelper {
    private static final Logger log = LoggerFactory.getLogger(CoordinatesInputHelper.class);
    private final CurrentInput currentInput;
    private final String owner;

    public FlatInputHelper(CurrentInput currentInput, String owner) {
        this.currentInput = currentInput;
        this.owner = owner;
    }

    public Flat getFlat() {
        System.out.println("Enter details for the flat. Type '\\q' at any prompt to quit.");

        String name = getName();
        if (name == null) return null; // Отмена операции


        Coordinates coordinates = new CoordinatesInputHelper(currentInput).getCoordinates();
        if (coordinates == null) return null; // Отмена операции

        float area = getPositiveFloat("Enter the area of the flat (must be greater than 0): ");
        if (area <= 0) return null; // Отмена операции

        long numberOfRooms = getPositiveLong("Enter the number of rooms (must be greater than 0): ");
        if (numberOfRooms <= 0) return null; // Отмена операции

        Float price = getPrice();
        if (price == null) return null; // Отмена операции

        View view = new ViewInputHelper(currentInput).getView();
        if (view == null && !confirmContinueWithoutOptional("View")) return null; // Отмена операции

        Transport transport = new TransportInputHelper(currentInput).getTransport();
        if (transport == null) return null; // Отмена операции

        House house = new HouseInputHelper(currentInput).getHouse();
        if (house == null) return null; // Отмена операции

        return new Flat(name, coordinates, area, numberOfRooms, price, view, transport, house, owner);
    }

    private String getName() {
        System.out.println("Enter the name of the flat (cannot be empty): ");
        while (true) {
            if (currentInput.getInputMode().equals(InputMode.FILE_MODE) && !currentInput.hasNextLine()){
                currentInput.setInputMode(InputMode.USER_MODE);
            }
            String input = currentInput.getNextLine().trim();
            if (input.equals("\\q")) {
                System.out.println("Operation cancelled by user.");
                return null;
            }
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Invalid input. The name cannot be empty.");
        }
    }

    private float getPositiveFloat(String prompt) {
        while (true) {
            System.out.println(prompt);
            if (currentInput.getInputMode().equals(InputMode.FILE_MODE) && !currentInput.hasNextLine()){
                currentInput.setInputMode(InputMode.USER_MODE);
            }
            String input = currentInput.getNextLine().trim();
            if (input.equals("\\q")) {
                System.out.println("Operation cancelled by user.");
                return -1;
            }
            try {
                float value = Float.parseFloat(input);
                if (value > 0) {
                    return value;
                } else {
                    System.out.println("Error: Value must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid floating-point number.");
            }
        }
    }

    private long getPositiveLong(String prompt) {
        while (true) {
            System.out.println(prompt);
            if (currentInput.getInputMode().equals(InputMode.FILE_MODE) && !currentInput.hasNextLine()){
                currentInput.setInputMode(InputMode.USER_MODE);
            }
            String input = currentInput.getNextLine().trim();
            if (input.equals("\\q")) {
                System.out.println("Operation cancelled by user.");
                return -1;
            }
            try {
                long value = Long.parseLong(input);
                if (value > 0) {
                    return value;
                } else {
                    System.out.println("Error: Value must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private Float getPrice() {
        System.out.println("Enter the price of the flat (must be greater than 0 or type '\\q' to skip): ");
        while (true) {
            if (currentInput.getInputMode().equals(InputMode.FILE_MODE) && !currentInput.hasNextLine()){
                currentInput.setInputMode(InputMode.USER_MODE);
            }
            String input = currentInput.getNextLine().trim();
            if (input.equals("\\q")) {
                return null;
            }
            try {
                float value = Float.parseFloat(input);
                if (value > 0) {
                    return value;
                } else {
                    System.out.println("Error: Price must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid floating-point number.");
            }
        }
    }

    private boolean confirmContinueWithoutOptional(String fieldName) {
        System.out.println("You have chosen to not specify the " + fieldName + ". Type 'yes' to continue without this field or 'no' to cancel:");
        while (true) {
            if (currentInput.getInputMode().equals(InputMode.FILE_MODE) && !currentInput.hasNextLine()){
                currentInput.setInputMode(InputMode.USER_MODE);
            }
            String input = currentInput.getNextLine().trim().toLowerCase();
            switch (input) {
                case "yes":
                    return true;
                case "no":
                    System.out.println("Operation cancelled by user.");
                    return false;
                default:
                    System.out.println("Please type 'yes' or 'no'.");
                    break;
            }
        }
    }

}
