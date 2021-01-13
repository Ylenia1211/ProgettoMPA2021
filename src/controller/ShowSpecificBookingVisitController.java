package controller;

import dao.ConcreteAppointmentDAO;
import dao.ConcreteOwnerDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import model.Appointment;
import model.Gender;
import model.Owner;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ShowSpecificBookingVisitController implements Initializable {
    public TableView<Appointment> tableBookingVisit;
    public TableColumn<Appointment, String>  col_data;
    public TableColumn<Appointment, String>  col_timestart;
    public TableColumn<Appointment, String>  col_timeend;
    public TableColumn<Appointment, String>  col_doctor;
    public TableColumn<Appointment, String>  col_type;
    public TableColumn<Appointment, String>  col_owner;
    public TableColumn<Appointment, String>  col_pet;
    private ConcreteAppointmentDAO appointmentRepo;
    public ObservableList<Appointment> listItems;


    public ShowSpecificBookingVisitController(List<Appointment> listAppointment) {
        this.listItems = FXCollections.observableArrayList(listAppointment);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableBookingVisit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        col_data.setCellValueFactory(new PropertyValueFactory<>("localDate"));
        col_timestart.setCellValueFactory(new PropertyValueFactory<>("localTimeStart"));
        col_timeend.setCellValueFactory(new PropertyValueFactory<>("localTimeEnd"));
        col_doctor.setCellValueFactory(new PropertyValueFactory<>("id_doctor"));  //questo si toglie visto che la vista deve essere specifica
                                                                                     //in base al doctor che visualizza
        col_type.setCellValueFactory(new PropertyValueFactory<>("specialitation"));  //nome dell'attributo nella classe
        col_owner.setCellValueFactory(new PropertyValueFactory<>("id_owner")); // #TODO: qui si dovrebbero mettere  dei bottoni: client, pet
        col_pet.setCellValueFactory(new PropertyValueFactory<>("id_pet"));
        try{
            ConnectionDBH2 connection = new ConnectionDBH2();
            appointmentRepo = new ConcreteAppointmentDAO(connection);
            tableBookingVisit.setItems(listItems);
            addButtonUpdateToTable();
            addButtonDeleteToTable();
        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }


    private void addButtonUpdateToTable() {
        TableColumn<Appointment, Void> colBtn = new TableColumn("");

        Callback<TableColumn<Appointment, Void>, TableCell<Appointment, Void>> cellFactory = new Callback<TableColumn<Appointment, Void>, TableCell<Appointment, Void>>() {
            @Override
            public TableCell<Appointment, Void> call(final TableColumn<Appointment, Void> param) {
                final TableCell<Appointment, Void> cell = new TableCell<Appointment, Void>() {

                    private final Button btn = new Button("Modifica");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Appointment data = getTableView().getItems().get(getIndex());

                            Scene scene = this.getScene();
                            BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/bookingAppointment.fxml"));
                                loader.setControllerFactory(new Callback<Class<?>, Object>() {
                                    public Object call(Class<?> p) {
                                        return  new UpdateBookingAppointmentController(data);
                                    }
                                });
                                borderPane.setCenter(loader.load());

                            } catch (IOException e) {
                                e.printStackTrace();
                            };

                            System.out.println("selectedData: " + data.getId() + " " + data.getLocalTimeStart());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);
        tableBookingVisit.getColumns().add(colBtn);
    }

    private void addButtonDeleteToTable() {
        TableColumn<Appointment, Void> colBtn = new TableColumn("");

        Callback<TableColumn<Appointment, Void>, TableCell<Appointment, Void>> cellFactory = new Callback<TableColumn<Appointment, Void>, TableCell<Appointment, Void>>() {
            @Override
            public TableCell<Appointment, Void> call(final TableColumn<Appointment, Void> param) {
                final TableCell<Appointment, Void> cell = new TableCell<Appointment, Void>() {
                    private final Button btn = new Button("Cancella");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Appointment data = getTableView().getItems().get(getIndex());
                            JPanel pan = new JPanel();
                            int ok = JOptionPane.showConfirmDialog(
                                    null,
                                    "Sei sicuro di voler cancellare?",
                                    "Cancellazione Prenotazione",
                                    JOptionPane.YES_NO_OPTION);
                            if(ok ==0) { //cancella
                                String id = appointmentRepo.search(data);
                                appointmentRepo.delete(id);
                                tableBookingVisit.getItems().remove(data); //elimina graficamente
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);
        tableBookingVisit.getColumns().add(colBtn);
    }
}
