package ru.nightmare.hex.controller.component;

import javafx.scene.image.WritableImage;
import com.google.gson.*;
public class Util {
    public static WritableImage screenshot;
    public static GsonBuilder builder = new GsonBuilder();
    public static Gson gson = builder.serializeNulls().create();
}
