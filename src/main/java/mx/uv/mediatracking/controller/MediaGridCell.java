package mx.uv.mediatracking.controller;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import mx.uv.mediatracking.model.Media;
import org.controlsfx.control.GridCell;
import java.io.InputStream;
import java.util.function.Consumer;

public class MediaGridCell extends GridCell<Media> {

    public enum CellType {
        SEARCH_RESULT,
        LIBRARY_ITEM
    }

    private final ImageView imageView = new ImageView();
    private final Label titleLabel = new Label();
    private final VBox vbox = new VBox(imageView, titleLabel);
    private final StackPane stackPane = new StackPane(vbox);

    private static Image placeholderImage;

    static {
        InputStream placeholderStream = MediaGridCell.class.getResourceAsStream("/mx/uv/mediatracking/view/placeholder.png");
        if (placeholderStream != null) {
            placeholderImage = new Image(placeholderStream);
        } else {
            System.err.println("¡Advertencia! No se pudo cargar placeholder.png.");
        }
    }

    public MediaGridCell(Consumer<Media> onAddCallback, CellType cellType) {
        imageView.setFitHeight(180);
        imageView.setFitWidth(120);
        imageView.setPreserveRatio(true);
        titleLabel.setWrapText(true);
        vbox.setAlignment(Pos.CENTER);

        if (cellType == CellType.SEARCH_RESULT) {
            Button addButton = new Button("Añadir +");
            addButton.getStyleClass().add("add-button-overlay");
            addButton.setVisible(false);

            stackPane.getChildren().add(addButton);

            stackPane.setOnMouseEntered(e -> addButton.setVisible(true));
            stackPane.setOnMouseExited(e -> addButton.setVisible(false));

            addButton.setOnAction(e -> {
                if (getItem() != null && onAddCallback != null) {
                    onAddCallback.accept(getItem());
                }
            });
        }

        setGraphic(stackPane);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(Media item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setVisible(false);
        } else {
            setVisible(true);
            String baseUrl = "https://image.tmdb.org/t/p/w200";
            if (item.getPosterPath() != null && !item.getPosterPath().isEmpty()) {
                imageView.setImage(new Image(baseUrl + item.getPosterPath()));
            } else {
                imageView.setImage(placeholderImage);
            }
            titleLabel.setText(item.getTitle());
        }
    }
}