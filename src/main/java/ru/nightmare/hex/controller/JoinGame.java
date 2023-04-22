package ru.nightmare.hex.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import ru.nightmare.hex.HexApplication;
import ru.nightmare.hex.controller.component.Camera;
import ru.nightmare.hex.controller.component.GamePreviewMouseHandler;
import ru.nightmare.hex.controller.component.Settings;
import ru.nightmare.hex.controller.component.Util;
import ru.nightmare.hex.model.GameStatus;
import ru.nightmare.hex.net.client.Client;
import ru.nightmare.hex.net.server.Server;

import java.io.IOException;

import static ru.nightmare.hex.controller.component.Camera.radius;
import static ru.nightmare.hex.controller.component.Util.world;

public class JoinGame {
    public Button connect;
    public Button cancel;
    public TextField ip;
    public Button clear;
    public TextField port;
    public TextField name;
    public Canvas canvas;
    public Label status;
    public Label players;
    public Label width;
    public Label height;
    public ColorPicker picker;
    boolean s = true;
    Timeline timeline = null;

    @FXML
    public void initialize() {
        Util.Audio.play(Util.Audio.lobby);
        Camera.reset();
        canvas.setOnMouseMoved(new GamePreviewMouseHandler(canvas));
        name.setText(Settings.getName());
        picker.setValue(new Color(Math.random(), Math.random(),Math.random(),Math.random()));


        timeline = new Timeline(new KeyFrame(Duration.seconds(0.3), event -> {

            if(world!=null) {
                try {

                    if(world.getGameStatus()== GameStatus.starting) {
                        if (s) {
                            Util.Audio.play(Util.Audio.countdown);
                            s = false;
                        }
                        status.setText(String.valueOf(world.getSecondsBeforeStart()));
                    } else {
                        status.setText(world.getGameStatus().toString());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    width.setText(Integer.toString(world.getWorldWidth()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    height.setText(Integer.toString(world.getWorldHeight()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    players.setText(Integer.toString(world.getPlayersCount()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if(world.getGameStatus()!=GameStatus.awaiting&&world.getGameStatus()!=GameStatus.starting) {
                        timeline.stop();
                        HexApplication.setScene("game-active.fxml");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        clear.setOnAction(actionEvent -> {
            ip.clear();
            name.clear();
            port.clear();
        });
        cancel.setOnAction(actionEvent -> {
            timeline.stop();
            HexApplication.setScene("main-menu.fxml");
        });
        connect.setOnAction(actionEvent -> {
            Settings.setName(name.getText());

            if(Util.world==null) {
                Util.world = new Client(ip.getText(), Integer.parseInt(port.getText()));
            } else {
                try {
                    Util.world.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Util.world = new Client(ip.getText(), Integer.parseInt(port.getText()));
            }
            /*try {
                Util.drawHexagons(radius, world.getVision(), 5, 5, canvas.getGraphicsContext2D());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
            Util.world.join();
            try {
                world.setColor(picker.getValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                world.setName(name.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                Util.drawHexagons(radius, world.getVision(), Camera.x, Camera.y, canvas.getGraphicsContext2D(), 0, 0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        }

}
