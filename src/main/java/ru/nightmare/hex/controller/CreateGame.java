package ru.nightmare.hex.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
import ru.nightmare.hex.net.server.Server;

import java.io.IOException;

import static ru.nightmare.hex.controller.component.Camera.radius;
import static ru.nightmare.hex.controller.component.Util.world;

public class CreateGame {
    public Canvas canvas;
    public Button create;
    public Button reset;
    public Button exit;
    public TextField name;
    public TextField port;
    public TextField width;
    public TextField height;
    public ColorPicker picker;

    public Label widthL;
    public Label heightL;
    public Label status;
    public Button generate;
    public Label players;
    public TextField playersCount;
    boolean s = true;

    @FXML
    public void initialize() throws IOException {
        Util.Audio.play(Util.Audio.lobby);

        name.setText(Settings.getName());
        picker.setValue(new Color(Math.random(), Math.random(),Math.random(),Math.random()));

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.3), event -> {

            if(world!=null) {
                try {

                    if(world.getGameStatus()==GameStatus.starting) {
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
                    widthL.setText(Integer.toString(world.getWorldWidth()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    heightL.setText(Integer.toString(world.getWorldHeight()));
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
                        HexApplication.setScene("game-active.fxml");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        canvas.setOnMouseMoved(new GamePreviewMouseHandler(canvas));

        generate.setOnAction(actionEvent -> {

            try {
                if(Util.world!=null) {
                    world.close();
                }
                Util.world = new Server(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()), Integer.parseInt(playersCount.getText()), Integer.parseInt(port.getText()), 2000);
                Util.drawHexagons(radius, world.getVision(), Camera.x, Camera.y, canvas.getGraphicsContext2D(), 0, 0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            /*try {
                Util.drawHexagons(radius, world.getVision(), 5, 5, canvas.getGraphicsContext2D());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/

        });
        create.setOnAction(actionEvent -> {
            Settings.setName(name.getText());
            try {

                if(Util.world==null) {
                    Util.world = new Server(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()), Integer.parseInt(playersCount.getText()), Integer.parseInt(port.getText()), 2000);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            /*try {
                Util.drawHexagons(radius, world.getVision(), 5, 5, canvas.getGraphicsContext2D());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
            try {
                Util.world.join();
            } catch (IllegalThreadStateException e) {
                try {
                    Util.world.close();
                    Util.world = new Server(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()), Integer.parseInt(playersCount.getText()), Integer.parseInt(port.getText()), 2000);
                    Util.world.join();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
            try {
                Util.drawHexagons(radius, world.getVision(), Camera.x, Camera.y, canvas.getGraphicsContext2D(), 0, 0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
        });
        reset.setOnAction(actionEvent -> {
            width.clear();
            height.clear();
            name.clear();
            port.clear();
            playersCount.clear();
        });
        exit.setOnAction(actionEvent -> {
            HexApplication.setScene("main-menu.fxml");
        });
    }

}
