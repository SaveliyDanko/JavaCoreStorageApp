package com.savadanko.client.modelinput;

import com.savadanko.client.input.CurrentInput;
import com.savadanko.client.input.InputMode;
import com.savadanko.common.models.Coordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoordinatesInputHelper {
    private static final Logger log = LoggerFactory.getLogger(CoordinatesInputHelper.class);
    private final CurrentInput currentInput;

    public CoordinatesInputHelper(CurrentInput currentInput) {
        this.currentInput = currentInput;
    }

    public Coordinates getCoordinates(){
        System.out.println("Enter the coordinates or '\\q' to quit:");

        Float x = getX();
        if (x == null) return null; // Пользователь выбрал выход.

        Long y = getY();
        if (y == null) return null; // Пользователь выбрал выход.

        return new Coordinates(x, y);
    }

    private Float getX() {
        while (true) {
            System.out.print("Enter the x coordinate (maximum 155): ");
            if (currentInput.getInputMode().equals(InputMode.FILE_MODE) && !currentInput.hasNextLine()){
                currentInput.setInputMode(InputMode.USER_MODE);
            }
            String input = currentInput.getNextLine();
            if (input.trim().equals("\\q")) {
                System.out.println("Operation cancelled by user.");
                return null;
            }
            try {
                float x = Float.parseFloat(input);
                if (x > 155) {
                    System.out.println("Error: The x coordinate cannot exceed 155.");
                } else {
                    return x;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid floating-point number.");
            }
        }
    }

    private Long getY() {
        while (true) {
            System.out.print("Enter the y coordinate (must be greater than -694): ");
            if (currentInput.getInputMode().equals(InputMode.FILE_MODE) && !currentInput.hasNextLine()){
                currentInput.setInputMode(InputMode.USER_MODE);
            }
            String input = currentInput.getNextLine();
            if (input.trim().equals("\\q")) {
                System.out.println("Operation cancelled by user.");
                return null;
            }
            try {
                long y = Long.parseLong(input);
                if (y <= -694) {
                    System.out.println("Error: The y coordinate must be greater than -694.");
                } else {
                    return y;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
}
