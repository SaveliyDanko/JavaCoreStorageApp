package com.savadanko.client;

import com.savadanko.common.dto.CommandProperties;
import com.savadanko.common.dto.Request;
import com.savadanko.common.dto.Response;
import com.savadanko.common.models.*;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ClientManager {
    @Getter
    private Scene scene;
    private final LinkedHashMap<Long, Flat> flatMap;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final String login;
    private TextArea messageArea; // Область для вывода сообщений
    private TableView<Flat> tableView;

    public ClientManager(
            Map<String, CommandProperties> commandMap,
            LinkedHashMap<Long, Flat> flatMap,
            ObjectOutputStream out,
            ObjectInputStream in,
            String login) {
        this.flatMap = flatMap;
        this.out = out;
        this.in = in;
        this.login = login;
        createScene(commandMap);
        startSyncTimer();
    }

    private void createScene(Map<String, CommandProperties> commandMap) {
        ComboBox<String> comboBox = createComboBox(commandMap);
        tableView = createTableView();
        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setWrapText(true);
        VBox vbox = new VBox(10, comboBox, tableView, messageArea);
        scene = new Scene(vbox, 800, 600);
    }

    private ComboBox<String> createComboBox(Map<String, CommandProperties> commandMap) {
        ComboBox<String> comboBox = new ComboBox<>();
        for (Map.Entry<String, CommandProperties> entry : commandMap.entrySet()) {
            comboBox.getItems().add(entry.getKey());
        }
        comboBox.setOnAction(e -> {
            String selectedCommand = comboBox.getSelectionModel().getSelectedItem();
            if (selectedCommand != null) {
                int argsCount = commandMap.get(selectedCommand).getArgsCount();
                boolean isObject = commandMap.get(selectedCommand).isObject();
                handleCommand(selectedCommand, argsCount, isObject);
            }
        });
        return comboBox;
    }

    private void handleCommand(String commandName, int argsCount, boolean isObject) {
        if (argsCount == 0 && !isObject) {
            Request request = new Request(commandName, new String[0], null, login);
            requestSender(request);
        } else if (argsCount != 0 && !isObject) {
            handleArgsInput(false, commandName);
        } else if (argsCount == 0) {
            handleObjectInput(commandName, new String[0]);
        } else {
            handleArgsInput(true, commandName);
        }
    }

    private void requestSender(Request request){
        try {
            out.writeObject(request);
            out.flush();

            Response response = (Response) in.readObject();
            // Вывод сообщения из response в TextArea
            messageArea.appendText(response.getMessage() + "\n");

        } catch (IOException | ClassNotFoundException e) {
            messageArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }

    private void handleArgsInput(boolean needsObject, String commandName) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Arguments");
        dialog.setHeaderText("Please enter the arguments:");
        dialog.setContentText("Arguments:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(args -> {
            if (needsObject) {
                handleObjectInput(commandName, new String[]{result.get()});
            } else {
                Request request = new Request(commandName, new String[]{result.get()}, null, login);
                requestSender(request);
            }
        });
    }

    private void handleObjectInput(String commandName, String[] args) {
        Dialog<Flat> dialog = new Dialog<>();
        dialog.setTitle("Input Object");
        dialog.setHeaderText("Please enter the object details:");

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        TextField nameField = new TextField();
        TextField xField = new TextField();
        TextField yField = new TextField();
        TextField areaField = new TextField();
        TextField roomsField = new TextField();
        TextField priceField = new TextField();
        ComboBox<View> viewComboBox = new ComboBox<>();
        viewComboBox.getItems().addAll(View.values());
        ComboBox<Transport> transportComboBox = new ComboBox<>();
        transportComboBox.getItems().addAll(Transport.values());
        TextField houseNameField = new TextField();
        TextField houseYearField = new TextField();
        TextField houseFloorsField = new TextField();
        TextField houseFlatsField = new TextField();
        TextField houseLiftsField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Coordinate X:"), 0, 1);
        grid.add(xField, 1, 1);
        grid.add(new Label("Coordinate Y:"), 0, 2);
        grid.add(yField, 1, 2);
        grid.add(new Label("Area:"), 0, 3);
        grid.add(areaField, 1, 3);
        grid.add(new Label("Number of Rooms:"), 0, 4);
        grid.add(roomsField, 1, 4);
        grid.add(new Label("Price:"), 0, 5);
        grid.add(priceField, 1, 5);
        grid.add(new Label("View:"), 0, 6);
        grid.add(viewComboBox, 1, 6);
        grid.add(new Label("Transport:"), 0, 7);
        grid.add(transportComboBox, 1, 7);
        grid.add(new Label("House Name:"), 0, 8);
        grid.add(houseNameField, 1, 8);
        grid.add(new Label("House Year:"), 0, 9);
        grid.add(houseYearField, 1, 9);
        grid.add(new Label("Number of Floors:"), 0, 10);
        grid.add(houseFloorsField, 1, 10);
        grid.add(new Label("Flats on Floor:"), 0, 11);
        grid.add(houseFlatsField, 1, 11);
        grid.add(new Label("Number of Lifts:"), 0, 12);
        grid.add(houseLiftsField, 1, 12);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                try {
                    Flat flat = new Flat();
                    flat.setName(validateString(nameField.getText(), "Name"));
                    Coordinates coordinates = new Coordinates();
                    coordinates.setX(validateFloat(xField.getText(), 155));
                    coordinates.setY(validateLong(yField.getText(), "Coordinate Y", -694L, null));
                    flat.setCoordinates(coordinates);
                    flat.setArea(validateFloat(areaField.getText(), "Area"));
                    flat.setNumberOfRooms(validateLong(roomsField.getText(), "Number of Rooms"));
                    flat.setPrice(validateFloat(priceField.getText(), "Price"));
                    flat.setView(viewComboBox.getValue());
                    flat.setTransport(validateNotNull(transportComboBox.getValue(), "Transport"));
                    House house = new House();
                    house.setName(houseNameField.getText());
                    house.setYear(validateLong(houseYearField.getText(), "House Year"));
                    house.setNumberOfFloors(validateLong(houseFloorsField.getText(), "Number of Floors"));
                    house.setNumberOfFlatsOnFloor(validateInt(houseFlatsField.getText(), "Flats on Floor"));
                    house.setNumberOfLifts(validateInt(houseLiftsField.getText(), "Number of Lifts"));
                    flat.setHouse(house);
                    flat.setOwner(login);

                    Request request = new Request(commandName, args, flat, login);
                    requestSender(request);

                } catch (IllegalArgumentException e) {
                    showErrorAlert(e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private String validateString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
        return value;
    }

    private float validateFloat(String value, String fieldName) {
        try {
            float floatValue = Float.parseFloat(value);
            if (floatValue <= 0) {
                throw new IllegalArgumentException(fieldName + " must be greater than 0");
            }
            return floatValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid number");
        }
    }

    private float validateFloat(String value, float maxValue) {
        float floatValue = validateFloat(value, "Coordinate X");
        if (floatValue > maxValue) {
            throw new IllegalArgumentException("Coordinate X" + " must be less than or equal to " + maxValue);
        }
        return floatValue;
    }

    private long validateLong(String value, String fieldName) {
        try {
            long longValue = Long.parseLong(value);
            if (longValue <= 0) {
                throw new IllegalArgumentException(fieldName + " must be greater than 0");
            }
            return longValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid number");
        }
    }

    private long validateLong(String value, String fieldName, Long minValue, Long maxValue) {
        long longValue = validateLong(value, fieldName);
        if (minValue != null && longValue <= minValue) {
            throw new IllegalArgumentException(fieldName + " must be greater than " + minValue);
        }
        if (maxValue != null && longValue > maxValue) {
            throw new IllegalArgumentException(fieldName + " must be less than or equal to " + maxValue);
        }
        return longValue;
    }

    private int validateInt(String value, String fieldName) {
        try {
            int intValue = Integer.parseInt(value);
            if (intValue <= 0) {
                throw new IllegalArgumentException(fieldName + " must be greater than 0");
            }
            return intValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid number");
        }
    }

    private <T> T validateNotNull(T value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
        return value;
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private TableView<Flat> createTableView() {
        ObservableList<Flat> flatList = FXCollections.observableArrayList(flatMap.values());
        TableView<Flat> tableView = new TableView<>();

        // Добавление столбцов в TableView
        tableView.getColumns().addAll(
                createColumn("ID", "id"),
                createColumn("Name", "name"),
                createCustomColumn("X", flat -> new SimpleFloatProperty(flat.getCoordinates().getX()).asObject()),
                createCustomColumn("Y", flat -> new SimpleLongProperty(flat.getCoordinates().getY()).asObject()),
                createTimestampColumn(),
                createCustomColumn("Area", flat -> new SimpleFloatProperty(flat.getArea()).asObject()),
                createCustomColumn("NumberOfRooms", flat -> new SimpleLongProperty(flat.getNumberOfRooms()).asObject()),
                createCustomColumn("Price", flat -> new SimpleFloatProperty(flat.getPrice()).asObject()),
                createColumn("View", "view"),
                createColumn("Transport", "transport"),
                createCustomColumn("House Name", flat -> new SimpleStringProperty(flat.getHouse().getName())),
                createCustomColumn("Year", flat -> new SimpleLongProperty(flat.getHouse().getYear()).asObject()),
                createCustomColumn("Number Of Floors", flat -> new SimpleLongProperty(flat.getHouse().getNumberOfFloors()).asObject()),
                createCustomColumn("Number Of Flats On Floor", flat -> new SimpleIntegerProperty(flat.getHouse().getNumberOfFlatsOnFloor()).asObject()),
                createCustomColumn("Number Of Lifts", flat -> new SimpleIntegerProperty(flat.getHouse().getNumberOfLifts()).asObject()),
                createColumn("Owner", "owner")
        );

        // Установка данных в TableView
        tableView.setItems(flatList);

        return tableView;
    }


    private <T> TableColumn<Flat, T> createColumn(String title, String property) {
        TableColumn<Flat, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    private <T> TableColumn<Flat, T> createCustomColumn(String title, CustomValueFactory<Flat, T> valueFactory) {
        TableColumn<Flat, T> column = new TableColumn<>(title);
        column.setCellValueFactory(cellData -> valueFactory.apply(cellData.getValue()));
        return column;
    }

    private TableColumn<Flat, String> createTimestampColumn() {
        TableColumn<Flat, String> column = new TableColumn<>("Timestamp");
        column.setCellValueFactory(cellData -> {
            ZonedDateTime timestamp = cellData.getValue().getCreationDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
            return new SimpleStringProperty(timestamp.format(formatter));
        });
        return column;
    }

    @FunctionalInterface
    private interface CustomValueFactory<S, T> {
        javafx.beans.value.ObservableValue<T> apply(S source);
    }

    private void startSyncTimer() {
        Timer syncTimer = new Timer(true);
        syncTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                syncWithDatabase();
            }
        }, 0, 1000);
    }

    private void syncWithDatabase() {
        Request request = new Request("sync", new String[0], null, login);
        try {
            out.writeObject(request);
            out.flush();
            Response response = (Response) in.readObject();
            if (response.getFlatMap() != null) {
                updateData(convertMap(response.getFlatMap()));
            }
        } catch (IOException | ClassNotFoundException e) {
            messageArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }

    private void updateData(LinkedHashMap<Long, Flat> newData) {
        flatMap.clear();
        flatMap.putAll(newData);
        updateTableView();
    }

    private void updateTableView() {
        ObservableList<Flat> flatList = FXCollections.observableArrayList(flatMap.values());
        tableView.setItems(flatList);
    }

    private LinkedHashMap<Long, Flat> convertMap(LinkedHashMap<Long, Object> inputMap) {
        LinkedHashMap<Long, Flat> resultMap = new LinkedHashMap<>();

        for (Map.Entry<Long, Object> entry : inputMap.entrySet()) {
            Long key = entry.getKey();
            Object value = entry.getValue();

            try {
                Flat flatValue = (Flat) value;
                resultMap.put(key, flatValue);
            } catch (Exception e) {
                System.err.println("Failed to convert value to Flat for key: " + key);
                System.out.println(e.getMessage());
            }
        }
        return resultMap;
    }
}
