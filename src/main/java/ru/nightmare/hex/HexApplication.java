package ru.nightmare.hex;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nightmare.hex.controller.component.Util;

import java.io.IOException;

public class HexApplication extends Application {

    public static Stage stage = null;
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HexApplication.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());

        //stage.setFullScreen(true);
        //stage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.F11));
        stage.setScene(scene);
        stage.setTitle("Hex");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (Util.world!=null)
            Util.world.close();
    }

    public static void setScene(String name) {
        FXMLLoader fxmlLoader = new FXMLLoader(HexApplication.class.getResource(name));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().addAll(HexApplication.class.getResource("style.css").toExternalForm());

            HexApplication.stage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        launch();
    }
}