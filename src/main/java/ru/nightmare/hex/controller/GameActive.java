package ru.nightmare.hex.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;
import ru.nightmare.hex.HexApplication;
import ru.nightmare.hex.controller.component.Camera;
import ru.nightmare.hex.controller.component.RenderType;
import ru.nightmare.hex.controller.component.Util;
import ru.nightmare.hex.model.Action;
import ru.nightmare.hex.model.GameStatus;
import ru.nightmare.hex.model.Hex;

import java.io.IOException;

import static ru.nightmare.hex.controller.component.Camera.*;
import static ru.nightmare.hex.controller.component.Util.limitValue;

public class GameActive {
    public Canvas map;
    public Button minimapRender;
    public Canvas minimap;
    public Button pause;
    public Button exit;
    public Button fourth;
    public Button units;
    public Button seventh;
    public Button second;
    public Button third;
    public Button fifth;
    public Button sixth;
    public Button eighth;
    public Button ninth;
    public Button first;
    public Button buildings;
    public Button attack;
    public Button stop;
    public Button move;
    public Button portal;
    public Button warp;
    public Button build;
    public Label resources;
    public ProgressBar energy;
    public Label players;
    private Action action;
    private double mX = 0, mY = 0;
    int n = 0;
    boolean pressed = false;

    @FXML
    public void initialize() {

        Util.Audio.play(Util.Audio.battle1);
        GraphicsContext gc = map.getGraphicsContext2D();

        pause.setOnAction(e -> {
            try {
                if (Util.world.getGameStatus()==GameStatus.paused) {
                    pause.setText("pause");
                    Util.world.continue_();
                } else {
                    pause.setText("continue");
                    Util.world.pause();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        exit.setOnAction(e -> {
            try {
                Util.world.close();
                HexApplication.setScene("main-menu.fxml");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


        /*map.setOnMouseMoved(e -> {
            //mX = e.getX();
            //mY = e.getY();
            //Hex[][] vs= Util.bufferedVision;
            System.out.println(e);
            //if (vs!=null&&Util.bufferedVision.length>0)
                //Util.drawHexagons(radius, vs, Camera.x, Camera.y, gc, mX, mY);
        });*/

        map.setOnScroll(e -> {
            if (e.getDeltaY()!=0) {
                Camera.zooming((float) e.getDeltaY() / 40);
            }
        });
        map.setOnMouseDragged(e -> {
            if (!pressed) {
                mX = e.getX();
                mY = e.getY();
            }
            pressed = true;
            Camera.move((float) (mX-e.getX()), (float) (mY-e.getY()));
            mX = e.getX();
            mY = e.getY();
            Hex[][] vs= Util.bufferedVision;
            Camera.x = (float) limitValue(Camera.x, -100, map.getWidth());
            Camera.y = (float) limitValue(Camera.y, -100, map.getHeight());
        });
        map.setOnMouseReleased(e -> {
            if (e.getButton()==(MouseButton.PRIMARY)) {
                pressed = false;
            }
        });

        /*map.setOnMouseClicked(e -> {
            Hex[][] vs= Util.bufferedVision;
            if (vs!=null&&Util.bufferedVision.length>0)
                Util.drawHexagons(radius, vs, Camera.x, Camera.y, gc, mX, mY, RenderType.full);
        });*/
        Timeline render = new Timeline(new KeyFrame(Duration.seconds(0.02), event -> {
            Hex[][] vs= Util.bufferedVision;
            if (vs!=null&&Util.bufferedVision.length>0) {
                Util.drawHexagons(radius, vs, Camera.x, Camera.y, gc, mX, mY, RenderType.full);
                Util.drawHexagons(minRadius, vs, Camera.x, Camera.y, minimap.getGraphicsContext2D(), 0, 0);
            }
        }));
        render.setCycleCount(Animation.INDEFINITE);
        render.play();
        Timeline net = new Timeline(new KeyFrame(Duration.seconds(0.06), event -> {
            Camera.step();
            gc.clearRect(0, 0, map.getWidth(), map.getHeight());
            minimap.getGraphicsContext2D().clearRect(0, 0, minimap.getWidth(), minimap.getHeight());
            try {
                Util.bufferedVision = Util.world.getVision();
                if (action!=null) {
                    Util.world.act(action);
                    action=null;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        net.setCycleCount(Animation.INDEFINITE);
        net.play();
    }
}
