package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
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

    /**
     * Create a anchor pane node. Date is not assigned in the constructor.
     * @param children children of the anchor pane
     */
    public AnchorPaneNode(Node... children) {
        super(children);
        // Add action handler for mouse clicked
        this.setOnMouseClicked(e -> {
            try {
//                root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("dayView.fxml")));
                FXMLLoader loader = new FXMLLoader(getClass().getResource("dayView.fxml"));
                Parent root = loader.load();
                DayView dayView = loader.getController();
                ZoneId defaultZoneId = ZoneId.systemDefault();
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                dayView.getDay(formatter.format(Date.from(date.atStartOfDay(defaultZoneId).toInstant())));
                Stage stage = new Stage();
                stage.setTitle("Appuntamenti di oggi");
                stage.setScene(new Scene(root, 450, 450));
                stage.setResizable(true);
                stage.show();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }});
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
