package com.savadanko.client.input;

import lombok.Getter;
import lombok.Setter;



public class CurrentInput implements IInput{
    private final IInput userInput;
    private final IInput fileInput;
    @Getter
    @Setter
    private InputMode inputMode;

    public CurrentInput(IInput userInput, IInput fileInput) {
        this.userInput = userInput;
        this.fileInput = fileInput;
        this.inputMode = InputMode.USER_MODE;
    }

    @Override
    public String getNextLine() {
        if (inputMode.equals(InputMode.USER_MODE)){
            return userInput.getNextLine();
        }
        else return fileInput.getNextLine();
    }

    @Override
    public boolean hasNextLine() {
        if (inputMode.equals(InputMode.USER_MODE)){
            return userInput.hasNextLine();
        }
        else return fileInput.hasNextLine();
    }

}
