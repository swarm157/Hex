package ru.nightmare.hex.controller.component;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import com.google.gson.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import ru.nightmare.hex.model.Hex;
import ru.nightmare.hex.model.Point;
import ru.nightmare.hex.model.Type;
import ru.nightmare.hex.net.World;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static ru.nightmare.hex.controller.component.Camera.radius;
import static ru.nightmare.hex.controller.component.Camera.zoom;

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
                if (hex.getOwnerId()>=0) {
                    Image img = null;
                    switch (hex.getType()) {
                    case empty -> {
                        break;
                    }
                    case factory -> {
                        img = Source.factory.getImage();
                        break;
                    }
                    case refinery -> {
                        img =Source.refinery.getImage();
                        break;
                    }
                    case watchTower -> {
                        img =Source.watchTower.getImage();
                        break;
                    }
                    case defenseTower -> {
                        img =Source.defenseTower.getImage();
                        break;
                    }
                    case harvester -> {
                        img =Source.harvester.getImage();
                        break;
                    }
                    case resource -> {
                        img =Source.resource.getImage();
                        break;
                    }
                    case solarPanel -> {
                        img =Source.solarPanel.getImage();
                        break;
                    }
                    case powerPlant -> {
                        img =Source.powerPlant.getImage();
                        break;
                    }
                    case waterMill -> {
                        img =Source.waterMill.getImage();
                        break;
                    }
                    case waterPomp -> {
                        img =Source.waterPomp.getImage();
                        break;
                    }
                    case miner -> {
                        img =Source.miner.getImage();
                        break;
                    }
                    case netTower -> {
                        img =Source.netTower.getImage();
                        break;
                    }
                    case infantry -> {
                        img =Source.infantry.getImage();
                        break;
                    }
                    case rocketSquad -> {
                        img =Source.rocketSquad.getImage();
                        break;
                    }
                    case buggy -> {
                        img =Source.buggy.getImage();
                        break;
                    }
                    case tank -> {
                        img =Source.tank.getImage();
                        break;
                    }
                    case ship -> {
                        img =Source.ship.getImage();
                        break;
                    }
                    case portal -> {
                        img =Source.portal.getImage();
                        break;
                    }
                    case warpMachine -> {
                        img =Source.warpMachine.getImage();
                        break;
                    }
                    case spaceShip -> {
                        img =Source.spaceShip.getImage();
                        break;
                    }
                    case builder -> {
                        img =Source.builder.getImage();
                        break;
                    }
                    case animal -> {
                        img =Source.animal.getImage();
                        break;
                    }
                }
                if (img!=null)
                    gc.drawImage(img, x * zoom - radius / 1.5 * zoom, y * zoom - radius / 1.5 * zoom, radius * zoom, radius * zoom);
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
                    xPoints[i] = x*zoom + radius * Math.cos(angle)*zoom;
                    yPoints[i] = y*zoom + radius * Math.sin(angle)*zoom;
                }

                double xC = findAverage(xPoints);
                double yC = findAverage(yPoints);

                // выбираем цвет для заливки и обводки в зависимости от номера
                gc.setStroke(Color.BLACK);
                if(!isPointInRange(xC, yC, (radius-0.5)*zoom, mX, mY))
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

    public static List<Hex> hexRange(Hex[][] matrix, double range, int xC, int yC) {
        List<Hex> found = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for(int o = 0; o < matrix[0].length; o++)
                if (isPointInRange(xC, yC, range, i, o)) {
                    found.add(matrix[i][o]);
                }
        }
        return found;
    }

    public static List<Point> pointRange(Hex[][] matrix, double range, int xC, int yC) {
        List<Point> found = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for(int o = 0; o < matrix[0].length; o++)
                if (isPointInRange(xC, yC, range, i, o)) {
                    found.add(new Point(i, o));
                }
        }
        return found;
    }

    public static boolean isHexInRange(int xC, int yC, double range, int x, int y) {
        return isPointInRange(xC, yC, range, x, y);
    }

    public static Path findPath(Hex[][] matrix, Point start, Point finish) {

        Node root = new Node(start, pointRange(matrix, 1.1, start.getX(), start.getY()));
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
    public static double limitValue(double value, double min, double max) {
        if (value<min) value = min;
        if (value>max) value = max;
        return value;
    }
    public static boolean isOutOfRange(double value, double min, double max) {
        if (value<min) return true;
        if (value>max) return true;
        return false;
    }
    public static boolean isOutOfRange(int value, int min, int max) {
        if (value<min) return true;
        if (value>max) return true;
        return false;
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
