package ru.nightmare.hex.model;

import javafx.scene.paint.Color;

public class Player {
    String name;
    int resources;
    int energy = 0;
    boolean defeated = false;
    Color color;
    public Player(int resources, Color color) {
        name = "unnamed";
        this.resources = resources;
        this.color = color;
    }
}
