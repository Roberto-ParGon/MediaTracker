package mx.uv.mediatracking.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import mx.uv.mediatracking.model.ApiService;
import mx.uv.mediatracking.model.Media;
import org.controlsfx.control.GridView;

import java.util.List;

public class MainController {

    @FXML private org.controlsfx.control.textfield.CustomTextField searchField;
    @FXML private Button searchButton;
    @FXML private GridView<Media> libraryGridView;
    @FXML private Label statusLabel;

    private final ApiService apiService = new ApiService();

    @FXML
    public void initialize() {
        libraryGridView.setCellFactory(param -> new MediaGridCell());
        libraryGridView.setCellWidth(150);
        libraryGridView.setCellHeight(240);
        libraryGridView.setHorizontalCellSpacing(15);
        libraryGridView.setVerticalCellSpacing(15);
        statusLabel.setText("Bienvenido a tu biblioteca.");
    }

    @FXML
    private void handleSearchAction() {
        String query = searchField.getText();
        if (query == null || query.trim().isEmpty()) {
            statusLabel.setText("Por favor, introduce un término de búsqueda.");
            return;
        }

        Task<List<Media>> searchTask = new Task<>() {
            @Override
            protected List<Media> call() throws Exception {
                return apiService.searchMedia(query);
            }
        };

        searchTask.setOnSucceeded(event -> {
            List<Media> results = searchTask.getValue();
            ObservableList<Media> mediaList = FXCollections.observableArrayList(results);
            libraryGridView.setItems(mediaList);
            statusLabel.setText("Se encontraron " + results.size() + " resultados.");
        });

        searchTask.setOnFailed(event -> {
            statusLabel.setText("Error en la búsqueda. Revisa tu conexión o la API Key.");
            searchTask.getException().printStackTrace();
        });

        statusLabel.setText("Buscando \"" + query + "\"...");
        new Thread(searchTask).start();
    }
}