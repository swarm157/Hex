package ru.nightmare.hex.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import ru.nightmare.hex.HexApplication;
import ru.nightmare.hex.controller.component.Util;

import java.io.IOException;

public class MainMenu {
    @FXML
    public Button exit;
    @FXML
    public Button create;
    @FXML
    public Button connect;

    @FXML
    public void initialize() {
        Util.Audio.play(Util.Audio.menu);
        exit.setOnAction(actionEvent -> {
            System.exit(0);
        });
        connect.setOnAction(actionEvent -> {
            HexApplication.setScene("join-game.fxml");
        });
        create.setOnAction(actionEvent -> {
            HexApplication.setScene("create-game.fxml");
        });
    }
}
