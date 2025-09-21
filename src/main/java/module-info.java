module mx.uv.mediatracking {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.net.http;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;

    opens mx.uv.mediatracking.controller to javafx.fxml;

    opens mx.uv.mediatracking.model to javafx.base, com.google.gson;

    exports mx.uv.mediatracking;
}