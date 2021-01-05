package controller;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import view.FullCalendarView;
import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    public HBox calendarPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        calendarPane.getChildren().add(new FullCalendarView(YearMonth.now()).getView());
    }
}
