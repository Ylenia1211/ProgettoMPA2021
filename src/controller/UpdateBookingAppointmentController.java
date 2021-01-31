package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import model.Appointment;
import util.email.ConcreteObserver;
import util.email.Observer;
import util.email.Subject;

import javax.swing.*;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UpdateBookingAppointmentController extends BookingAppointmentController implements Subject {
    private Appointment appointment;
    private String id;  //mi serve per l'update nel dao

    private final List<Observer> observers;

    public UpdateBookingAppointmentController(Appointment appointment) {
        super();
        this.observers = new ArrayList<>();
        this.appointment = appointment;
        this.id = super.getAppointmentRepo().search(appointment);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        super.getLabelTitle().setText("Modifica Data/Ora Prenotazione");


        //voglio modificare solo la data e/0 l'ora quindi elimino gli altri campi
        super.pane_main_grid.getChildren().clear();
        super.pane_main_grid.getChildren().add(super.getTextdateVisit());
        super.getTextdateVisit().setValue(this.appointment.getLocalDate());
        super.addFieldTimeStart();
        super.addFieldMinutesTimeStart();
        super.addFieldTimeDuration();
        super.addButtonSave();
        this.addActionButton();
        setParam(this.appointment);
    }

    //mi serve per settare i parametri di modifica con i dati precedentementi salvati
    private void setParam(Appointment appointment) {
        super.getTextdateVisit().setValue(appointment.getLocalDate());
        super.getTextTimeStart().setValue(appointment.getLocalTimeStart().minusMinutes(appointment.getLocalTimeStart().getMinute()));
        super.getTextMinutesTimeStart().setValue(appointment.getLocalTimeStart().getMinute());
        super.getTextTimeDuration().setValue((int) Duration.between(appointment.getLocalTimeStart(), appointment.getLocalTimeEnd()).toMinutes());
    }

    @Override
    public void addActionButton() {
        this.btn.setOnAction(this::updateVisit);
    }

    @Override
    public void register(Observer o) {
        observers.add(o);
    }

    @Override
    public void unregister(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer obs : observers) {
            obs.update();
        }
    }

    private Appointment createAppointmentForUpdate() {
        //creo un oggetto appuntamento tale da modificare solo data e/o ora
        // var timeStartVisit = LocalTime.of((Integer)this.getTextTimeStart().getValue(),(Integer) this.getTextMinutesTimeStart().getValue());
        LocalTime timeStartVisit = ((LocalTime) this.getTextTimeStart().getValue()).plusMinutes((Integer) this.getTextMinutesTimeStart().getValue());
        //System.out.println(this.dataVisit.getValue());
        //System.out.println((timeStartVisit).plusMinutes((Integer)super.getTextTimeDuration().getValue())); //fine
        return new Appointment.Builder()
                .setLocalDate(super.getTextdateVisit().getValue())
                .setLocalTimeStart(timeStartVisit)
                .setLocalTimeEnd((timeStartVisit).plusMinutes((Integer) super.getTextTimeDuration().getValue()))
                .setId_doctor(this.appointment.getId_doctor())
                .setSpecialitation(this.appointment.getSpecialitation())
                .setId_owner(this.appointment.getId_owner())
                .setId_pet(this.appointment.getId_pet())
                .build();


    }

    private void updateVisit(ActionEvent actionEvent) {

        Appointment p = createAppointmentForUpdate();
        //if ((p.getLocalTimeStart().isAfter(LocalTime.now()) && p.getLocalDate().isEqual(LocalDate.now())) || p.getLocalDate().isAfter(LocalDate.now())) {
        if ((p.getLocalTimeStart().isBefore(LocalTime.now()) && p.getLocalDate().isEqual(LocalDate.now())) ) {
            JOptionPane.showMessageDialog(null, "Impossibile inserire prenotazione: l'orario deve essere successivo all'orario attuale!");
        }
        else{
            if (!(p.getLocalDate().isEqual(this.appointment.getLocalDate())) ||
                    ((p.getLocalTimeStart().compareTo(this.appointment.getLocalTimeStart())) != 0) ||
                    ((p.getLocalTimeEnd().compareTo(this.appointment.getLocalTimeEnd())) != 0)) {


                List<Appointment> listAppointment = this.getAppointmentRepo().searchVisitbyDoctorAndDate(this.appointment.getId_doctor(), p.getLocalDate().toString());
                List<Appointment> listWithoutItSelf = listAppointment.stream().filter(
                        item -> !item.getId_owner().equals(this.appointment.getId_owner()) &&
                                !item.getId_pet().equals(this.appointment.getId_pet())).collect(Collectors.toList());

                boolean isValid = listWithoutItSelf.stream().allMatch(item -> (item.getLocalTimeStart().isAfter(p.getLocalTimeStart()) &&
                        (item.getLocalTimeStart().isAfter(p.getLocalTimeEnd()) || item.getLocalTimeStart().equals(p.getLocalTimeEnd()))) || //intervallo sinistro

                        ((item.getLocalTimeEnd().isBefore(p.getLocalTimeStart()) || item.getLocalTimeEnd().equals(p.getLocalTimeStart())) && //intervallo destro
                                item.getLocalTimeEnd().isBefore(p.getLocalTimeEnd()))
                );
                if (isValid) {
                    this.getAppointmentRepo().update(this.id, p);
                    this.appointment = p; //per aggiornare l'appuntamento nella view

                    String emailOwner = this.getAppointmentRepo().searchEmailOwnerbyIdAppointment(this.id);
                    ConcreteObserver observerChanges = new ConcreteObserver.Builder()
                            .setEmailOwner(emailOwner) //passare email owner associatato alla prenotazione
                            .setDataVisit(p.getLocalDate())
                            .setTimeStartVisit(p.getLocalTimeStart())
                            .setTimeEndVisit(p.getLocalTimeEnd())
                            .build();

                    this.register(observerChanges);
                    this.notifyObservers();
                } else {
                    JOptionPane.showMessageDialog(null, "Impossibile inserire la prenotazione. Un altro appuntamento è già stato prenotato per quell'intervallo di tempo!");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Nessuna modifica effettuata! La data e l'ora sono uguali a quelle già registrate!");
                alert.setTitle("Nessuna modifica effettuata!");
                alert.showAndWait();
            }
        }

    }
}
