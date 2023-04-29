package ru.nightmare.hex.controller.component;

import ru.nightmare.hex.model.Point;

import java.util.List;

public class Node {

    Node root = null;
    Point point;
    List<Point> linked;

    public Node(Point point, List<Point> linked) {
        this.point = point;
        this.linked = linked;
    }

    public Node(Node root, Point point, List<Point> linked) {
        this.root = root;
        this.point = point;
        this.linked = linked;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public List<Point> getLinked() {
        return linked;
    }

    public void setLinked(List<Point> linked) {
        this.linked = linked;
    }
    public boolean isFinal(Point finish) {
        return linked.contains(finish);
    }
    public boolean isRoot() {
        return root==null;
    }
}
