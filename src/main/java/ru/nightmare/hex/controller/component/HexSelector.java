package ru.nightmare.hex.controller.component;

import ru.nightmare.hex.model.Hex;
import ru.nightmare.hex.model.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.nightmare.hex.controller.component.Util.world;

public class HexSelector {
    List<Point> points;
    Hex[][] matrix;

    public HexSelector(List<Point> points, Hex[][] matrix) {
        this.matrix = matrix;
        this.points = points;
    }

    public HexSelector enemy(boolean with) throws IOException {
        List<Point> filtered = new ArrayList<>();
        for(Point point: points) {
            Hex hex = matrix[point.getX()][point.getY()];
            boolean enemy = (hex.getOwnerId()==-1)?false:(hex.getOwnerId()!= world.getMyId());
            if (with) {
                if (enemy)
                    filtered.add(point);
            } else {
                if (!enemy)
                    filtered.add(point);
            }
        }
        return new HexSelector(filtered, matrix);
    }
    public HexSelector controlled(boolean with) throws IOException {
        List<Point> filtered = new ArrayList<>();
        for(Point point: points) {
            Hex hex = matrix[point.getX()][point.getY()];
            boolean controlled = hex.getOwnerId()!=-1;
            if (with) {
                if (controlled)
                    filtered.add(point);
            } else {
                if (!controlled)
                    filtered.add(point);
            }
        }
        return new HexSelector(filtered, matrix);
    }
    public HexSelector mine(boolean with) throws IOException {
        List<Point> filtered = new ArrayList<>();
        for(Point point: points) {
            Hex hex = matrix[point.getX()][point.getY()];
            boolean mine = hex.getOwnerId()==world.getMyId();
            if (with) {
                if (mine)
                    filtered.add(point);
            } else {
                if (!mine)
                    filtered.add(point);
            }
        }
        return new HexSelector(filtered, matrix);
    }
    public HexSelector ownedBy(boolean with, int id) throws IOException {
        List<Point> filtered = new ArrayList<>();
        for(Point point: points) {
            Hex hex = matrix[point.getX()][point.getY()];
            boolean ownedBy = hex.getOwnerId()==id;
            if (with) {
                if (ownedBy)
                    filtered.add(point);
            } else {
                if (!ownedBy)
                    filtered.add(point);
            }
        }
        return new HexSelector(filtered, matrix);
    }


    public List<Point> getPoints() {
        return points;
    }
}
