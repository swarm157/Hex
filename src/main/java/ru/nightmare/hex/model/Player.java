package ru.nightmare.hex.model;

import javafx.scene.paint.Color;

public class Player {
    String name;
    int resources;
    int energy = 0;
    boolean defeated = false;

    private static int getLastId() {
        return lastId;
    }

    public int getId() {
        return id;
    }

    int id;
    Color color;
    public Player(int resources, Color color) {
        name = "unnamed";
        this.resources = resources;
        this.color = color;
        id = genId();
    }
    private static int lastId = 0;
    public static int genId() {
        lastId++;
        return lastId-1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResources() {
        return resources;
    }

    public void setResources(int resources) {
        this.resources = resources;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public boolean isDefeated() {
        return defeated;
    }

    public void setDefeated(boolean defeated) {
        this.defeated = defeated;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
