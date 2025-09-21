package mx.uv.mediatracking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mx.uv.mediatracking.model.DatabaseService;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        DatabaseService.initializeDatabase();

        String fxmlPath = "/mx/uv/mediatracking/view/MainView.fxml";
        URL fxmlLocation = Main.class.getResource(fxmlPath);

        if (fxmlLocation == null) {
            System.err.println("No se pudo encontrar el archivo FXML en la ruta: " + fxmlPath);
            System.exit(1);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        String cssPath = "/mx/uv/mediatracking/view/styles.css";
        URL cssLocation = Main.class.getResource(cssPath);
        if (cssLocation != null) {
            scene.getStylesheets().add(cssLocation.toExternalForm());
        } else {
            System.err.println("No se pudo encontrar el archivo CSS en la ruta: " + cssPath);
        }

        primaryStage.setTitle("Mi Biblioteca Multimedia");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}