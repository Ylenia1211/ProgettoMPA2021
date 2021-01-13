package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import model.Appointment;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class UpdateBookingAppointmentController extends BookingAppointmentController{
    private Appointment appointment;
    private Label labelView;
    private DatePicker dataVisit;
    private String id;  //mi serve per l'update nel dao

    public UpdateBookingAppointmentController(Appointment appointment) {
        super();
        this.appointment = appointment;
        this.id = super.getAppointmentRepo().search(appointment);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        //super.labelTitle.setText("Modifica Data/Ora Prenotazione");
        this.labelView = new Label("Modifica Data/Ora Prenotazione");
        this.labelView.setTextFill(Paint.valueOf("a6a6a6"));
        this.labelView.setFont(Font.font("Calibre", 30));


        this.dataVisit = new DatePicker();
        //posso selezionare solo le date a partire da Oggi
        this.dataVisit.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0 );
            }
        });

        //voglio modificare solo la data e/0 l'ora quindi elimino gli altri campi
        super.pane_main_grid.getChildren().clear();

        super.pane_main_grid.getChildren().add(this.labelView);
        super.pane_main_grid.getChildren().add(this.dataVisit);
        super.addFieldTimeStart();
        super.addFieldTimeDuration();
        super.addButtonSave();
        this.addActionButton();
        setParam(this.appointment);
    }

    //mi serve per settare i parametri di modifica con i dati precedentementi salvati
    private void setParam(Appointment appointment) {
        this.dataVisit.setValue(appointment.getLocalDate());
        super.getTextTimeStart().setValue(appointment.getLocalTimeStart());
        super.getTextTimeDuration().setValue(appointment.getLocalTimeEnd().getMinute());
    }

    @Override
    public void addActionButton() {
        this.btn.setOnAction(this::updateVisit);
    }

    private void updateVisit(ActionEvent actionEvent) {
        Appointment p = createAppointmentForUpdate();

        //controllo che almeno uno tra la data o la data di inizio o la data di fine SIANO CAMBIATI
        if( !(p.getLocalDate().isEqual(this.appointment.getLocalDate()))  ||
             ((p.getLocalTimeStart().compareTo(this.appointment.getLocalTimeStart()))!=0) ||
                ((p.getLocalTimeEnd().compareTo(this.appointment.getLocalTimeEnd()))!=0)
        ){
            //se uno tra data/ora inizio/ora fine è cambiata allora effettuo l'update
            this.getAppointmentRepo().update(this.id, p); //#todo: aggiungere l'observer se la data e/o l'ora è cambiata
            //System.out.println("id owner: " + this.appointment.getId_owner());
            //passare a concrete observer:
            // l'indirizzo email del owner associato
            // data, ora inizio, ora fine (prevista) della visita

        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Nessuna modifica effettuata! La data e l'ora sono uguali a quelle già registrate!");
            alert.setTitle("Nessuna modifica effettuata!");
            alert.showAndWait();
        }

    }

    private Appointment createAppointmentForUpdate(){
        //creo un oggetto appuntamento tale da modificare solo data e/o ora
        Appointment p = new Appointment.Builder()
                .setLocalDate(this.dataVisit.getValue())
                .setLocalTimeStart((LocalTime) super.getTextTimeStart().getValue())
                .setLocalTimeEnd(((LocalTime) super.getTextTimeStart().getValue()).plusMinutes((Integer)super.getTextTimeDuration().getValue()))
                .build();
        return p;
    }

}
