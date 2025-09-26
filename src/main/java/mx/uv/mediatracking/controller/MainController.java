package mx.uv.mediatracking.controller;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.util.Duration;
import mx.uv.mediatracking.model.ApiService;
import mx.uv.mediatracking.model.DatabaseService;
import mx.uv.mediatracking.model.Media;
import org.controlsfx.control.GridView;
import org.controlsfx.control.Notifications;

import java.util.List;

public class MainController {

    @FXML private org.controlsfx.control.textfield.CustomTextField searchField;
    @FXML private ScrollPane libraryScrollPane;
    @FXML private GridView<Media> libraryGridView;
    @FXML private ScrollPane searchResultsScrollPane;
    @FXML private GridView<Media> searchResultsGridView;
    @FXML private Label mainContentLabel;
    @FXML private Button backButton;
    @FXML private Label statusLabel;

    private final ApiService apiService = new ApiService();
    private final DatabaseService dbService = new DatabaseService();

    @FXML
    public void initialize() {
        libraryGridView.setCellFactory(param -> new MediaGridCell(null, MediaGridCell.CellType.LIBRARY_ITEM));
        configureGridView(libraryGridView);

        searchResultsGridView.setCellFactory(param -> new MediaGridCell(this::addMediaToLibrary, MediaGridCell.CellType.SEARCH_RESULT));
        configureGridView(searchResultsGridView);

        statusLabel.setText("Bienvenido a tu biblioteca.");
        loadLibrary();
    }

    private void loadLibrary() {
        Task<List<Media>> loadTask = new Task<>() {
            @Override
            protected List<Media> call() {
                return dbService.getAllMediaFromLibrary();
            }
        };
        loadTask.setOnSucceeded(e -> {
            libraryGridView.setItems(FXCollections.observableArrayList(loadTask.getValue()));
        });
        new Thread(loadTask).start();
    }

    private void configureGridView(GridView<Media> gridView) {
        gridView.setCellWidth(150);
        gridView.setCellHeight(240);
        gridView.setHorizontalCellSpacing(15);
        gridView.setVerticalCellSpacing(15);
    }

    private void addMediaToLibrary(Media media) {
        try {
            dbService.saveMedia(media);
            showNotification("Añadido a la biblioteca", media);
            loadLibrary();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotification(String title, Media media) {
        Notifications notificationBuilder = Notifications.create()
                .title(title)
                .text(media.getTitle())
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(actionEvent -> {
                    try {
                        dbService.deleteMedia(media);
                        loadLibrary();
                        Notifications.create().title("Acción deshecha").position(Pos.BOTTOM_RIGHT).showInformation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        notificationBuilder.showConfirm();
    }

    @FXML
    private void handleSearchAction() {
        String query = searchField.getText();
        if (query == null || query.trim().isEmpty()) {
            statusLabel.setText("Por favor, introduce un término de búsqueda.");
            return;
        }

        libraryScrollPane.setVisible(false);
        searchResultsScrollPane.setVisible(true);
        mainContentLabel.setText("Resultados de búsqueda");
        backButton.setVisible(true);

        Task<List<Media>> searchTask = new Task<>() {
            @Override
            protected List<Media> call() throws Exception {
                return apiService.searchMedia(query);
            }
        };

        searchTask.setOnSucceeded(event -> {
            List<Media> results = searchTask.getValue();
            searchResultsGridView.setItems(FXCollections.observableArrayList(results));
            statusLabel.setText("Se encontraron " + results.size() + " resultados.");
        });

        searchTask.setOnFailed(event -> {
            statusLabel.setText("Error en la búsqueda. Revisa tu conexión o la API Key.");
            searchTask.getException().printStackTrace();
        });

        statusLabel.setText("Buscando \"" + query + "\"...");
        new Thread(searchTask).start();
    }

    @FXML
    private void handleBackAction() {
        searchField.clear();
        searchResultsGridView.getItems().clear();

        searchResultsScrollPane.setVisible(false);
        backButton.setVisible(false);
        libraryScrollPane.setVisible(true);
        mainContentLabel.setText("Mi Biblioteca");
        statusLabel.setText("Listo.");
    }
}