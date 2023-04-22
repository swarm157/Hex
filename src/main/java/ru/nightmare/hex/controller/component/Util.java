package ru.nightmare.hex.controller.component;

import javafx.scene.image.WritableImage;
import com.google.gson.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import ru.nightmare.hex.model.Hex;
import ru.nightmare.hex.model.Type;
import ru.nightmare.hex.net.World;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.io.IOException;

import static ru.nightmare.hex.controller.component.Camera.radius;

public class Util {
    public static WritableImage screenshot;
    public static GsonBuilder builder = new GsonBuilder();
    public static Gson gson = builder.serializeNulls().create();

    public static World world;
    public static Hex[][] bufferedVision;

    public static int[] getHexagonByCoord(double radius, double coordX, double coordY, double offsetX, double offsetY) {
        double q = (coordX - offsetX) / (Math.sqrt(3) * radius);
        double r = ((coordY - offsetY) - q * radius * 0.5) / (radius * 1.5);

        int xIndex = (int) Math.round(q);
        int yIndex = (int) Math.round(r);

        double dx = Math.abs(q - xIndex);
        double dy = Math.abs(r - yIndex);
        double mz = Math.abs((-q - r) + (xIndex + yIndex));

        if (dx > dy && dx > mz) {
            xIndex = -yIndex - xIndex;
        } else if (dy > mz) {
            yIndex = -xIndex - yIndex;
        }

        return new int[] { yIndex, xIndex };
    }

    public static void drawHexagons(double radius, Hex[][] matrix, double startX, double startY, GraphicsContext gc, double mX, double mY) {
        drawHexagons(radius, matrix, startX, startY, gc, mX, mY, RenderType.biomes);
    }

    private static void drawBiomes(Hex hex, GraphicsContext gc, double[] xPoints, double[] yPoints) {
        switch (hex.getBiome()) {

            case empty -> {
                gc.setFill(Color.BLACK);
                break;
            }
            case forest -> {
                gc.setFill(Color.FORESTGREEN);
                break;
            }
            case water -> {
                gc.setFill(Color.SEASHELL);
                break;
            }
            case desert -> {
                gc.setFill(Color.PALEGOLDENROD);
                break;
            }
            case mountain -> {
                gc.setFill(Color.SILVER);
                break;
            }
            case glitchedLands -> {
                gc.setFill(Color.WHITESMOKE);
                break;
            }
            case plain -> {
                gc.setFill(Color.GREEN);
                break;
            }
            case air -> {
                gc.setFill(Color.SKYBLUE);
                break;
            }
        }
        gc.fillPolygon(xPoints, yPoints, 6);
        gc.strokePolygon(xPoints, yPoints, 6);
    }
    private static void drawPlayerColor(Hex hex, GraphicsContext gc, double[] xPoints, double[] yPoints) {
        if (hex.getType()!=null&&!hex.getType().equals(Type.empty)) {
            try {
                if (hex.getOwnerId()>=0)
                    if (world.getPlayers().get(hex.getOwnerId()).getColor()==null) {
                        gc.setFill(world.getPlayers().get(hex.getOwnerId()).getColor());
                        gc.fillPolygon(xPoints, yPoints, 6);
                        gc.strokePolygon(xPoints, yPoints, 6);
                    }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private static void drawPics(Hex hex, GraphicsContext gc, double x, double y) {
                if (hex.getOwnerId()>=0) switch (hex.getType()) {
                    case empty -> {
                        break;
                    }
                    case factory -> {
                        gc.drawImage(Source.factory.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case refinery -> {
                        gc.drawImage(Source.refinery.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case watchTower -> {
                        gc.drawImage(Source.watchTower.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case defenseTower -> {
                        gc.drawImage(Source.defenseTower.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case harvester -> {
                        gc.drawImage(Source.harvester.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case resource -> {
                        gc.drawImage(Source.resource.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case solarPanel -> {
                        gc.drawImage(Source.solarPanel.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case powerPlant -> {
                        gc.drawImage(Source.powerPlant.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case waterMill -> {
                        gc.drawImage(Source.waterMill.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case waterPomp -> {
                        gc.drawImage(Source.waterPomp.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case miner -> {
                        gc.drawImage(Source.miner.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case netTower -> {
                        gc.drawImage(Source.netTower.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case infantry -> {
                        gc.drawImage(Source.infantry.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case rocketSquad -> {
                        gc.drawImage(Source.rocketSquad.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case buggy -> {
                        gc.drawImage(Source.buggy.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case tank -> {
                        gc.drawImage(Source.tank.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case ship -> {
                        gc.drawImage(Source.ship.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case portal -> {
                        gc.drawImage(Source.portal.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case warpMachine -> {
                        gc.drawImage(Source.warpMachine.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case spaceShip -> {
                        gc.drawImage(Source.spaceShip.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case builder -> {
                        gc.drawImage(Source.builder.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                    case animal -> {
                        gc.drawImage(Source.animal.getImage(), x-radius/1.5, y-radius/1.5, radius, radius);
                        break;
                    }
                }
    }
    public static void drawHexagons(double radius, Hex[][] matrix, double startX, double startY, GraphicsContext gc, double mX, double mY, RenderType type) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;
        double height = (3 * radius) / 2;
        double width = Math.sqrt(3) * radius;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                double x = startX + col * width + (row % 2) * (width / 2);
                double y = startY + row * height;
                //Hex num = matrix[row][col];

                double[] xPoints = new double[6];
                double[] yPoints = new double[6];

                Polygon hexagon = new Polygon();
                // вычисляем координаты вершин шестиугольника
                for (int i = 0; i < 6; i++) {
                    double angle = 2 * Math.PI / 6 * i;
                    xPoints[i] = x + radius * Math.cos(angle);
                    yPoints[i] = y + radius * Math.sin(angle);
                }

                double xC = findAverage(xPoints);
                double yC = findAverage(yPoints);

                // выбираем цвет для заливки и обводки в зависимости от номера
                gc.setStroke(Color.BLACK);
                if(!isPointInRange(xC, yC, radius-0.5, mX, mY))
                    switch (type) {

                        case full -> {
                            drawBiomes(matrix[row][col], gc, xPoints, yPoints);
                            drawPlayerColor(matrix[row][col], gc, xPoints, yPoints);
                            drawPics(matrix[row][col], gc, x, y);
                            break;
                        }
                        case biomes -> {
                            drawBiomes(matrix[row][col], gc, xPoints, yPoints);
                            break;
                        }
                        case players -> {
                            drawPlayerColor(matrix[row][col], gc, xPoints, yPoints);
                            drawPics(matrix[row][col], gc, x, y);
                            break;
                        }
                    }

                else {
                    gc.setFill(Color.YELLOW);
                    gc.fillPolygon(xPoints, yPoints, 6);
                    gc.strokePolygon(xPoints, yPoints, 6);
                }

                // рисуем шестиугольник

                // добавляем номер шестиугольника
                //gc.setFill(Color.BLACK);
                //gc.fillText(matrix[row][col]+"", x-5, y+5);
            }
        }
    }
    public static double findAverage(double[] numbers) {
        double sum = 0.0;
        for (double number : numbers) {
            sum += number;
        }
        double average = sum / numbers.length;
        return average;
    }
    public static boolean isPointInRange(double centerX, double centerY, double radius, double pointX, double pointY) {
        double distance = Math.sqrt(Math.pow((pointX - centerX), 2) + Math.pow((pointY - centerY), 2));
        return distance <= radius;
    }

    public static class Audio {
        public static final String countdown =  "sound/game-start-countdown-SBA-300420112-preview.mp3";
        public static final String lobby =  "sound/Lobby-Time.mp3";
        public static final String menu  =  "sound/Space-Jazz.mp3";
        public static final String battle1 =  "sound/battle/Enigma-Long-Version-Complete-Version.mp3";
        public static final String battle2  =  "sound/battle/Fall-From-Grace.mp3";
        public static final String battle3 =  "sound/battle/BossBattle_0073_BPM110_Cm_Hydra-and-Ash_L.mp3";
        private static MediaPlayer mediaPlayer;

        public static void play(String name) {
            if (mediaPlayer!=null)
                mediaPlayer.stop();
            Media sound = new Media(new File(name).toURI().toString());
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
            mediaPlayer.setCycleCount(-1);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setVolume(20);
        }

    }


}
