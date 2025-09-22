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
    private final ImageView imageView = new ImageView();
    private final Label titleLabel = new Label();
    private final Button addButton = new Button("Añadir");
    private final VBox vbox = new VBox(imageView, titleLabel);
    private final StackPane stackPane = new StackPane();

    private static Image placeholderImage;

    private final Consumer<Media> onAddCallback;

    static {
        InputStream placeholderStream = MediaGridCell.class.getResourceAsStream("/mx/uv/mediatracking/view/placeholder.png");
        if (placeholderStream != null) {
            placeholderImage = new Image(placeholderStream);
        } else {
            System.err.println("¡Advertencia! No se pudo cargar placeholder.png.");
        }
    }

    public MediaGridCell(Consumer<Media> onAddCallback) {
        this.onAddCallback = onAddCallback;

        imageView.setFitHeight(180);
        imageView.setFitWidth(120);
        imageView.setPreserveRatio(true);
        titleLabel.setWrapText(true);
        vbox.setAlignment(Pos.CENTER);
        addButton.setVisible(false);

        stackPane.getChildren().addAll(vbox, addButton);
        setGraphic(stackPane);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        stackPane.setOnMouseEntered(e -> addButton.setVisible(true));
        stackPane.setOnMouseExited(e -> addButton.setVisible(false));

        addButton.setOnAction(e -> {
            if (getItem() != null) {
                onAddCallback.accept(getItem());
            }
        });
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