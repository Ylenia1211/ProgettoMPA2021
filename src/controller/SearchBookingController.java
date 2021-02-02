package controller;

import dao.ConcreteAppointmentDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import model.Appointment;
import util.gui.ButtonTable;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static util.gui.ButtonTable.addButtonDeleteToTable;
import static util.gui.ButtonTable.addButtonUpdateToTable;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Implementando i metodi di 'Inizializable' {@link Initializable} inizializza la view associata al controller.
 * Questa classe visualizza le prenotazioni future con la possibilità di cancellare, modificare, e visualizzare i dettagli delle prenotazioni.
 */
public class SearchBookingController implements Initializable {
    public TextField searchField;
    public TableView<Appointment> tableAllBookingVisit;
    public TableColumn<Appointment, String> col_data;
    public TableColumn<Appointment, String> col_timestart;
    public TableColumn<Appointment, String> col_timeend;
    public TableColumn<Appointment, String> col_type;
    public ImageView searchbtn;
    private final ConcreteAppointmentDAO appointmentRepo;
    public ObservableList<Appointment> listItems;

    /**
     * Costruttore della classe, setta l'attributo {@link SearchBookingController#appointmentRepo} con una nuova istanza
     * di {@link ConcreteAppointmentDAO} richiamando la Connessione singleton {@link ConnectionDBH2} del database.
     */
    public SearchBookingController() {
        this.appointmentRepo = new ConcreteAppointmentDAO(ConnectionDBH2.getInstance());
    }

    /**
     * Inizializza i campi della view in modo appropriato.
     * Visualizza i risultati della ricerca.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableAllBookingVisit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        col_data.setCellValueFactory(new PropertyValueFactory<>("localDate"));
        col_timestart.setCellValueFactory(new PropertyValueFactory<>("localTimeStart"));
        col_timeend.setCellValueFactory(new PropertyValueFactory<>("localTimeEnd"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("specialitation"));

        List<Appointment> listSuccAppointments = appointmentRepo.searchVisitAfterDate(LocalDate.now(), LocalTime.now());
        listItems = FXCollections.observableArrayList(Objects.requireNonNullElseGet(listSuccAppointments, ArrayList::new)); //devo visualuzzare solo le prenotaizoni non ancora passate
        tableAllBookingVisit.setItems(FXCollections.observableArrayList(Objects.requireNonNullElseGet(listItems, ArrayList::new)));

        var colBtnView = ButtonTable.addButtonViewInfoOwnerPet();
        tableAllBookingVisit.getColumns().add((TableColumn<Appointment, ?>) colBtnView);
        var colBtnUpdate = addButtonUpdateToTable("/view/bookingAppointment.fxml", -1);
        tableAllBookingVisit.getColumns().add((TableColumn<Appointment, ?>) colBtnUpdate);
        var colBtnDelete = addButtonDeleteToTable(tableAllBookingVisit, Appointment.class);
        tableAllBookingVisit.getColumns().add((TableColumn<Appointment, ?>) colBtnDelete);

        this.searchField.textProperty().addListener((observableFC, oldValueFC, newValueFC) -> {
            if ((newValueFC).matches(("[A-Za-z]*\s[A-Za-z]+"))) {
                searchbtn.setOnMouseClicked(event -> {
                    String[] fieldSplitted = newValueFC.toUpperCase().split("\s");
                    List<Appointment> succAppointmentsPet = this.appointmentRepo.findAllVisitPetAfterDate(fieldSplitted[0], fieldSplitted[1], LocalDate.now());
                    tableAllBookingVisit.setItems(FXCollections.observableArrayList(Objects.requireNonNullElseGet(succAppointmentsPet, ArrayList::new)));
                });
            } else {
                searchbtn.setOnMouseClicked(event -> JOptionPane.showMessageDialog(null, "Impossibile effettuare la ricerca!\n Controlla di aver inserito almeno:\n 2 parole: nome cognome\n 1 spazio tra le due parole"));
            }
        });
    }
}
