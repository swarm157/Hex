module ru.nightmare.hex {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.google.gson;

    opens ru.nightmare.hex to javafx.fxml, com.google.gson;
    exports ru.nightmare.hex;
    exports ru.nightmare.hex.controller;
    opens ru.nightmare.hex.controller to javafx.fxml;
    exports ru.nightmare.hex.model;
    opens ru.nightmare.hex.model to com.google.gson;

}