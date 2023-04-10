package ru.nightmare.hex.controller.component;

import javafx.scene.paint.Color;

import java.io.*;
import java.util.Properties;

public class Settings {

    private static Properties properties = new Properties();

    private static File file = new File("settings.properties");

    protected static String name;

    static {
        if (file.isDirectory()) {
            file.delete();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            properties.load(new FileInputStream(file));
            name = properties.getProperty("name");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Settings.name = name;
        properties.setProperty("name", Settings.name);
        try {
            properties.store(new FileWriter(file), "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
