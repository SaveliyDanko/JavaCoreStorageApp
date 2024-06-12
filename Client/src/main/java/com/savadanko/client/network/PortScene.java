package com.savadanko.client.network;

import com.savadanko.client.network.authorization.LoginScene;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PortScene {
    private final Stage stage;
    @Getter
    private Scene scene;

    public PortScene(Stage stage) {
        this.stage = stage;
        createScene();
    }

    private void createScene(){
        VBox vBox = new VBox();

        Label label1 = new Label("Enter port");
        Label warningPort = new Label();
        Label warningConnection = new Label();

        TextField textField = new TextField();
        textField.setPromptText("Enter port");

        Button button = new Button("Connect");


        button.setOnAction(actionEvent -> {
            String port = textField.getText();
            if (isNumeric(port)){
                warningPort.setText("Valid port: " + port);
                warningPort.setStyle("-fx-text-fill: green;");
                try {
                    Socket socket = new Socket("localhost", Integer.parseInt(port));
                    LoginScene loginScene = new LoginScene(
                            stage,
                            scene,
                            new ObjectOutputStream(socket.getOutputStream()),
                            new ObjectInputStream(socket.getInputStream()));

                    stage.setTitle("Login");
                    stage.setScene(loginScene.getScene());
                }
                catch (IOException e){
                    warningConnection.setText("Invalid connection, try again");
                    warningConnection.setStyle("-fx-text-fill: red;");
                }
            }
            else {
                warningPort.setText("Invalid input. Please enter a valid number.");
                warningPort.setStyle("-fx-text-fill: red;");
            }
        });

        vBox.getChildren().addAll(label1, textField, button, warningPort, warningConnection);

        scene = new Scene(vBox);
    }

    // Метод для проверки, является ли строка числом
    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}

