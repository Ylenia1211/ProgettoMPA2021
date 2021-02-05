package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import model.Appointment;
import model.Owner;
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

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Implementando i metodi di 'Inizializable' {@link Initializable} inizializza la view associata al controller.
 * Gestisce l'update di un Appointement, estende la classe {@link  BookingAppointmentController}  ereditandone i campi della view associata.
 * Implementa l'interfaccia {@link Subject} necessaria per l'aggiunta di oggetti osservabili.
 */
public class UpdateBookingAppointmentController extends BookingAppointmentController implements Subject {
    private Appointment appointment;
    private final String id;  //mi serve per l'update nel dao
    private final List<Observer> observers;

    /**
     * Il costruttore della classe inizializza la lista di oggetti ossevabili, assegna a {@link UpdateBookingAppointmentController#appointment} l'appuntamento passato a parametro, ed
     * a {@link UpdateBookingAppointmentController#id} l'id dell'appuntamento usando la funzione della superclasse
     * {@link BookingAppointmentController#getAppointmentRepo()}
     *
     * @param appointment l'appuntamento da modificare
     */
    public UpdateBookingAppointmentController(Appointment appointment) {
        super();
        this.observers = new ArrayList<>();
        this.appointment = appointment;
        this.id = super.getAppointmentRepo().search(appointment);

    }

    /**
     * Richiama la funzione {@link BookingAppointmentController#initialize(URL, ResourceBundle)} della superclasse e setta i
     * parametri della view grazie alla funzione {@link UpdateBookingAppointmentController#setParam(Appointment)}
     * <p>
     * {@inheritDoc}
     */
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

    /**
     * Inserisce i dati dell'appuntamento negli appositi campi
     *
     * @param appointment L'oggetto di tipo {@link Appointment}
     */
    //mi serve per settare i parametri di modifica con i dati precedentementi salvati
    private void setParam(Appointment appointment) {
        super.getTextdateVisit().setValue(appointment.getLocalDate());
        super.getTextTimeStart().setValue(appointment.getLocalTimeStart().minusMinutes(appointment.getLocalTimeStart().getMinute()));
        super.getTextMinutesTimeStart().setValue(appointment.getLocalTimeStart().getMinute());
        super.getTextTimeDuration().setValue((int) Duration.between(appointment.getLocalTimeStart(), appointment.getLocalTimeEnd()).toMinutes());
    }

    /**
     * Assegna al bottone l'azione {@link UpdateBookingAppointmentController#updateVisit(ActionEvent)} d'aggiornamento della visita.
     */
    @Override
    public void addActionButton() {
        this.btn.setOnAction(this::updateVisit);
    }


    /**
     * Aggiunge un oggetto di tipo Observer passato a parametro alla lista {@link UpdateBookingAppointmentController#observers}
     *
     * @param o L'oggetto di tipo Observer da aggiungere alla lista
     */
    @Override
    public void register(Observer o) {
        observers.add(o);
    }


    /**
     * Rimuove l'oggetto di tipo Observer passato a parametro alla lista {@link UpdateBookingAppointmentController#observers}
     *
     * @param o L'oggetto di tipo Observer da aggiungere alla lista
     */
    @Override
    public void unregister(Observer o) {
        observers.remove(o);
    }


    /**
     * Richiama il metodo update su tutti gli oggetti Observer inseriti nella lista {@link UpdateBookingAppointmentController#observers}
     */
    @Override
    public void notifyObservers() {
        for (Observer obs : observers) {
            obs.update();
        }
    }

    /**
     * Crea un oggetto di tipo Appointment con i parametri inseriti grazie al Builder
     *
     * @return Un oggetto di tipo Appointment
     */
    private Appointment createAppointmentForUpdate() {
        //creo un oggetto appuntamento tale da modificare solo data e/o ora
        LocalTime timeStartVisit = ((LocalTime) this.getTextTimeStart().getValue()).plusMinutes((Integer) this.getTextMinutesTimeStart().getValue());
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

    /**
     * Verifica se non si stia inserendo un Appuntamento non valido (sovrapposto con altri appuntamenti per lo stesso dottore e animale nel medesimo tempo) e aggiorna l'appuntamento.
     * Dopoddichè viene mandata un email di notifica all'utente associato alla prenotazione modificata.
     *
     * @param actionEvent L'evento registrato, in questo caso il click sul bottone
     */
    private void updateVisit(ActionEvent actionEvent) {

        Appointment p = createAppointmentForUpdate();

        if ((p.getLocalTimeStart().isBefore(LocalTime.now()) && p.getLocalDate().isEqual(LocalDate.now()))) { //controllo che non si inserisca un appuntamento precedente a oggi
            JOptionPane.showMessageDialog(null, "Impossibile inserire prenotazione: l'orario deve essere successivo all'orario attuale!");
        } else {
            if (!(p.getLocalDate().isEqual(this.appointment.getLocalDate())) ||
                    ((p.getLocalTimeStart().compareTo(this.appointment.getLocalTimeStart())) != 0) ||
                    ((p.getLocalTimeEnd().compareTo(this.appointment.getLocalTimeEnd())) != 0)) {


                List<Appointment> listAppointment = this.getAppointmentRepo().searchVisitbyDoctorAndDate(this.appointment.getId_doctor(), p.getLocalDate().toString());
                //non devo contare le sovrapposizioni con se stesso
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
                            .setEmailOwner(emailOwner) //passare email owner associato alla prenotazione
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
