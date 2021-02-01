package controller;

import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import view.AgendaView;

import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;

/**
 * La classe CalendarController inizializza la vista dell'agenda
 */
public class CalendarController implements Initializable {

    public HBox calendarPane;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        calendarPane.getChildren().add(new AgendaView(YearMonth.now()).getView());
    }
}
