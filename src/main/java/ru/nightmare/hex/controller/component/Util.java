package ru.nightmare.hex.controller.component;

import javafx.scene.image.WritableImage;
import com.google.gson.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ru.nightmare.hex.model.Hex;
import ru.nightmare.hex.net.World;

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

                // вычисляем координаты вершин шестиугольника
                for (int i = 0; i < 6; i++) {
                    double angle = 2 * Math.PI / 6 * i;
                    xPoints[i] = x + radius * Math.cos(angle);
                    yPoints[i] = y + radius * Math.sin(angle);
                }

                double xC = findAverage(xPoints);
                double yC = findAverage(yPoints);

                // выбираем цвет для заливки и обводки в зависимости от номера

                if(!isPointInRange(xC, yC, radius-0.5, mX, mY))
                switch (matrix[row][col].getBiome()) {

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
                else
                    gc.setFill(Color.YELLOW);

                gc.setStroke(Color.BLACK);


                // рисуем шестиугольник
                gc.fillPolygon(xPoints, yPoints, 6);
                gc.strokePolygon(xPoints, yPoints, 6);
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


}
