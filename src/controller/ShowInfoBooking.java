package controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.Appointment;

import java.net.URL;
import java.util.ResourceBundle;

public class ShowInfoBooking implements Initializable {
    public VBox main_booking_view;
    public Label dataText;
    public Label timestartText;
    public Label timeendText;
    private  Appointment appointment;

    public ShowInfoBooking(Appointment appointment) {
        this.appointment = appointment;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            this.dataText.setText(this.appointment.getLocalDate().toString());
            this.timestartText.setText(this.appointment.getLocalTimeStart().toString());
            this.timeendText.setText(this.appointment.getLocalTimeEnd().toString());
    }
}
