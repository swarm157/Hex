package ru.nightmare.hex.controller.component;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

import static ru.nightmare.hex.controller.component.Camera.radius;
import static ru.nightmare.hex.controller.component.Util.*;

public class GamePreviewMouseHandler implements EventHandler<MouseEvent> {


    Canvas canvas;
    public GamePreviewMouseHandler(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
// находим координаты мыши на Canvas
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();
        Canvas c = canvas;
        double canvasWidth = c.getWidth();
        double canvasHeight = c.getHeight();
        gc.clearRect(0, 0, canvasWidth, canvasHeight);// очищаем Canvas
        try {
            if(world!=null)
                Util.drawHexagons(radius, world.getVision(), Camera.x, Camera.y, canvas.getGraphicsContext2D(), 0, 0, RenderType.full);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }





        if (bufferedVision!=null)
            // отрисовываем шестиугольники
            drawHexagons(radius, bufferedVision, Camera.x, Camera.y, gc, mouseX, mouseY);



        // вычисляем номер шестиугольника
        /*int[] hex = getHexagonByCoord(radius, mouseX, mouseY, 50, 50);

        // выбираем цвет для заливки и обводки выделенного шестиугольника
        gc.setFill(Color.YELLOW);
        gc.setStroke(Color.BLACK);

        // вычисляем координаты вершин выделенного шестиугольника
        double[] xPoints = new double[6];
        double[] yPoints = new double[6];
        double x = 50 + hex[1] * Math.sqrt(3) * radius + (hex[0] % 2) * (Math.sqrt(3) * radius / 2);
        double y = 50 + hex[0] * (1.5 * radius);
        for (int i = 0; i < 6; i++) {
            double angle = 2 * Math.PI / 6 * i;
            xPoints[i] = x + radius * Math.cos(angle);
            yPoints[i] = y + radius * Math.sin(angle);
        }
*/
// рисуем выделенный шестиугольник
        //gc.fillPolygon(xPoints, yPoints, 6);
        //gc.strokePolygon(xPoints, yPoints, 6);
    }
}
