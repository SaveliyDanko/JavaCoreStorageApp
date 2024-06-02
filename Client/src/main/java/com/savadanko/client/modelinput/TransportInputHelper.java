package com.savadanko.client.modelinput;

import com.savadanko.client.input.CurrentInput;
import com.savadanko.client.input.InputMode;
import com.savadanko.common.models.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransportInputHelper {
    private static final Logger log = LoggerFactory.getLogger(ViewInputHelper.class);
    private final CurrentInput currentInput;

    public TransportInputHelper(CurrentInput currentInput) {
        this.currentInput = currentInput;
    }

    public Transport getTransport(){
        while (true){
            System.out.println("Enter transport (FEW, NONE, LITTLE, NORMAL) or '\\q' to quit:");
            if (currentInput.getInputMode().equals(InputMode.FILE_MODE) && !currentInput.hasNextLine()){
                currentInput.setInputMode(InputMode.USER_MODE);
            }
            String input = currentInput.getNextLine().trim().toUpperCase();
            if (input.equals("\\Q")){
                System.out.println("Operation canceled");
                return null;
            }
            try{
                return Transport.valueOf(input);
            }
            catch (IllegalArgumentException e){
                log.error(e.getMessage());
                System.out.println("Invalid input. Please enter one of the specified values.");
            }
        }
    }
}
