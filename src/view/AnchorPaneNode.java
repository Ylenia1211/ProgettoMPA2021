package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Create an anchor pane that can store additional data.
 */
public class AnchorPaneNode extends AnchorPane {

    // Date associated with this pane
    private LocalDate date;
    private Color color;

    /**
     * Create a anchor pane node. Date is not assigned in the constructor.
     * @param children children of the anchor pane
     */
    public AnchorPaneNode(Node... children) {
        super(children);
        this.setColor(Color.GREEN);

        // Add action handler for mouse clicked
        this.setOnMouseClicked(e -> {
            Scene scene = this.getScene();
            BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");
            try {
                borderPane.setCenter(FXMLLoader.load(getClass().getResource("/view/registrationClient.fxml")));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
