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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

//visualizza le prenotazioni gia effettutate con il riferimento ai report associati
public class SearchReportController  implements Initializable {
    public TextField searchField;
    public TableView<Appointment> tableAllBookingVisit;
    public TableColumn<Appointment, String> col_data;
    public TableColumn<Appointment, String> col_timestart;
    public TableColumn<Appointment, String> col_timeend;
    public TableColumn<Appointment, String> col_type;
    public ImageView searchbtn;
    private ConcreteAppointmentDAO appointmentRepo;
    public ObservableList<Appointment> listItems;
    public TableColumn<Appointment, Void> colBtnCreateReport= new TableColumn("");
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableAllBookingVisit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        col_data.setCellValueFactory(new PropertyValueFactory<>("localDate"));
        col_timestart.setCellValueFactory(new PropertyValueFactory<>("localTimeStart"));
        col_timeend.setCellValueFactory(new PropertyValueFactory<>("localTimeEnd"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("specialitation"));
        appointmentRepo = new ConcreteAppointmentDAO(ConnectionDBH2.getInstance());
        List<Appointment> listPrecAppointments = appointmentRepo.searchVisitBeforeDate(LocalDate.now());
        listItems = FXCollections.observableArrayList(listPrecAppointments); //devo visualuzzare solo le prenotaizoni gia passate < localdate.now()
        tableAllBookingVisit.setItems(FXCollections.observableArrayList(Objects.requireNonNullElseGet(listItems, ArrayList::new)));

        var colBtnDelete = ButtonTable.addButtonDeleteToTable(tableAllBookingVisit, Appointment.class);
        tableAllBookingVisit.getColumns().add((TableColumn<Appointment, ?>)colBtnDelete);
        //addButtonDeleteToTable();
        //addButtonViewInfoOwnerPet();
        var colBtnView = ButtonTable.addButtonViewInfoOwnerPet(tableAllBookingVisit);
        tableAllBookingVisit.getColumns().add((TableColumn<Appointment, ?>) colBtnView);
        //addButtonCreateReport();

        var colBtnCreateReport = ButtonTable.addButtonCreateReport(tableAllBookingVisit);
        tableAllBookingVisit.getColumns().add((TableColumn<Appointment, ?>) colBtnCreateReport);

        //addButtonViewReport();
        var colBtnViewReport = ButtonTable.addButtonViewReport(tableAllBookingVisit);
        tableAllBookingVisit.getColumns().add((TableColumn<Appointment, ?>)  colBtnViewReport);

        this.searchField.textProperty().addListener((observableFC, oldValueFC, newValueFC) -> {
            if ((newValueFC).matches(("[A-Za-z]*\s[A-Za-z]+"))){

                searchbtn.setOnMouseClicked(event -> {
                    String[] fieldSplitted = newValueFC.toUpperCase().split("\s");
                    List<Appointment> prevAppointmentsPet = this.appointmentRepo.findAllVisitPetBeforeDate(fieldSplitted[0],fieldSplitted[1], LocalDate.now());
                    //devo visualuzzare solo le prenotaizoni gia passate < localdate.now()
                    tableAllBookingVisit.setItems(FXCollections.observableArrayList(Objects.requireNonNullElseGet(prevAppointmentsPet, ArrayList::new)));
                });

            }
            else{
                searchbtn.setOnMouseClicked(event -> {
                   // System.out.println(" Non Effettuare la ricerca ");
                    JOptionPane.showMessageDialog(null, "Impossibile effettuare la ricerca!\n Controlla di aver inserito almeno:\n 2 parole: nome cognome\n 1 spazio tra le due parole");
                });
            }
        });
    }
}
