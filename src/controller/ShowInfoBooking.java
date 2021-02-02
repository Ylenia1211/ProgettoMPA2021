package controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Appointment;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Implementando i metodi di 'Inizializable' {@link Initializable} inizializza la view associata al controller.
 * Mostra le informazioni relative alla visita.
 */
public class ShowInfoBooking implements Initializable {
    public Label dataText;
    public Label timestartText;
    public Label timeendText;
    private final Appointment appointment;

    /**
     * Il costruttore della classe ShowInfoBooking, inizializza tutti i campi relativi a un appuntamento e crea
     * un oggetto di tipo appointment.
     * @param  appointment appuntamento di cui vogliamo visualizzare le informazioni.
     */
    public ShowInfoBooking(Appointment appointment) {
        this.appointment = appointment;
    }
    /**
     * Inizializza i campi della view in modo appropriato
     *
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.dataText.setText(this.appointment.getLocalDate().toString());
        this.timestartText.setText(this.appointment.getLocalTimeStart().toString());
        this.timeendText.setText(this.appointment.getLocalTimeEnd().toString());
    }
}
