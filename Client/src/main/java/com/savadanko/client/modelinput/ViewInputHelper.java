package com.savadanko.client.modelinput;

import com.savadanko.client.input.CurrentInput;
import com.savadanko.client.input.InputMode;
import com.savadanko.common.models.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewInputHelper {
    private static final Logger log = LoggerFactory.getLogger(ViewInputHelper.class);
    private final CurrentInput currentInput;

    public ViewInputHelper(CurrentInput currentInput) {
        this.currentInput = currentInput;
    }

    public View getView(){
        while (true){
            System.out.println("Enter view (STREET, YARD, PARK, NORMAL) or '\\q' to quit:");
            if (currentInput.getInputMode().equals(InputMode.FILE_MODE) && !currentInput.hasNextLine()){
                currentInput.setInputMode(InputMode.USER_MODE);
            }
            String input = currentInput.getNextLine().trim().toUpperCase();
            if (input.equals("\\Q")){
                System.out.println("Operation canceled");
                return null;
            }
            try{
                return View.valueOf(input);
            }
            catch (IllegalArgumentException e){
                log.error(e.getMessage());
                System.out.println("Invalid input. Please enter one of the specified values.");
            }
        }
    }
}
