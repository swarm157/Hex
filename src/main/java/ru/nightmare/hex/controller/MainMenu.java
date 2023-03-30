package ru.nightmare.hex.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenu {
    @FXML
    public Button exit;
    @FXML
    public Button create;
    @FXML
    public Button connect;

    @FXML
    public void initialize() {
        exit.setOnAction(actionEvent -> {
            System.exit(0);
        });
        connect.setOnAction(actionEvent -> {

        });
        create.setOnAction(actionEvent -> {

        });
    }
}
