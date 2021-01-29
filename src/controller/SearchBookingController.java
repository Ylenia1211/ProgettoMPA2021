package controller;

import dao.ConcreteAppointmentDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import model.Appointment;
import util.gui.ButtonTable;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static util.gui.ButtonTable.addButtonDeleteToTable;
import static util.gui.ButtonTable.addButtonUpdateToTable;

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

    public SearchBookingController() {
        this.appointmentRepo = new ConcreteAppointmentDAO(ConnectionDBH2.getInstance());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableAllBookingVisit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        col_data.setCellValueFactory(new PropertyValueFactory<>("localDate"));
        col_timestart.setCellValueFactory(new PropertyValueFactory<>("localTimeStart"));
        col_timeend.setCellValueFactory(new PropertyValueFactory<>("localTimeEnd"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("specialitation"));
        List<Appointment> listSuccAppointments = appointmentRepo.searchVisitAfterDate(LocalDate.now()); //.plusDays(1));
        listItems = FXCollections.observableArrayList(listSuccAppointments); //devo visualuzzare solo le prenotaizoni non ancora passate
        tableAllBookingVisit.setItems(FXCollections.observableArrayList(Objects.requireNonNullElseGet(listItems, ArrayList::new)));
       // addButtonViewInfoOwnerPet(); //#todo: fare il refactor su questi metodi
        var colBtnView = ButtonTable.addButtonViewInfoOwnerPet(tableAllBookingVisit);
        tableAllBookingVisit.getColumns().add((TableColumn<Appointment, ?>) colBtnView);
        var colBtnUpdate = addButtonUpdateToTable("/view/bookingAppointment.fxml", tableAllBookingVisit, -1);
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
