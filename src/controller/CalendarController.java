package controller;

import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import view.AgendaView;

import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * La classe CalendarController inizializza la vista dell'agenda implementando i metodi di 'Inizializable' {@link Initializable}
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
