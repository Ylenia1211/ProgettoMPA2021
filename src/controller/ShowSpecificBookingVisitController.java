package controller;

import dao.ConcreteAppointmentDAO;
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
import util.gui.ButtonTable;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static util.gui.ButtonTable.addButtonDeleteToTable;

public class ShowSpecificBookingVisitController implements Initializable {
    public TableView<Appointment> tableBookingVisit;
    public TableColumn<Appointment, String>  col_data;
    public TableColumn<Appointment, String>  col_timestart;
    public TableColumn<Appointment, String>  col_timeend;
    public TableColumn<Appointment, String>  col_type;
    private ConcreteAppointmentDAO appointmentRepo;
    public ObservableList<Appointment> listItems;
    public TableColumn<Appointment, Void> colBtnCreateReport= new TableColumn("");

    public ShowSpecificBookingVisitController(List<Appointment> listAppointment) {
        this.listItems = FXCollections.observableArrayList(listAppointment);
    }
    //test
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableBookingVisit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        col_data.setCellValueFactory(new PropertyValueFactory<>("localDate"));
        col_timestart.setCellValueFactory(new PropertyValueFactory<>("localTimeStart"));
        col_timeend.setCellValueFactory(new PropertyValueFactory<>("localTimeEnd"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("specialitation"));  //nome dell'attributo nella classe
        appointmentRepo = new ConcreteAppointmentDAO(ConnectionDBH2.getInstance());

        tableBookingVisit.setItems(listItems);
        addButtonUpdateVisitToTable(); //non posso rifattorizzare cme gli altri (cambia l'update)
        //#todo, togliere il bottone di cancellazione della prenotazione per i dottori
        var colBtnDelete = addButtonDeleteToTable(tableBookingVisit, Appointment.class);
        tableBookingVisit.getColumns().add((TableColumn<Appointment, ?>)colBtnDelete);

        //addButtonViewInfoOwnerPet();
        var colBtnView = ButtonTable.addButtonViewInfoOwnerPet();
        tableBookingVisit.getColumns().add((TableColumn<Appointment, ?>) colBtnView);

        //addButtonCreateReport();
        var colBtnCreateReport = ButtonTable.addButtonCreateReport();
        tableBookingVisit.getColumns().add((TableColumn<Appointment, ?>) colBtnCreateReport);

        //addButtonViewReport();
        var colBtnViewReport = ButtonTable.addButtonViewReport();
        tableBookingVisit.getColumns().add((TableColumn<Appointment, ?>)  colBtnViewReport);

    }


    private void addButtonUpdateVisitToTable() {
        TableColumn<Appointment, Void> colBtn = new TableColumn("");
        Callback<TableColumn<Appointment, Void>, TableCell<Appointment, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Appointment, Void> call(final TableColumn<Appointment, Void> param) {
                final TableCell<Appointment, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Modifica");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Appointment data = getTableView().getItems().get(getIndex());
                            //System.out.println("Print idOwner prenotazione" + data.getId_owner());
                            Scene scene = this.getScene();
                            BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/bookingAppointment.fxml"));
                                loader.setControllerFactory(p -> new UpdateBookingAppointmentController(data));
                                borderPane.setCenter(loader.load());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);

                        } else {
                            Appointment ap = getTableColumn().getTableView().getItems().get(getIndex());
                            String id_appointment = appointmentRepo.search(ap);
                            //todo modifica solo se è il dottore loggato lo stesso della prenotazione
                            if (appointmentRepo.searchIfExistAppointmentInReport(id_appointment) || getTableColumn().getTableView().getItems().get(getIndex()).getLocalDate().isBefore(LocalDate.now().plusDays(1))) {
                                setGraphic(null);
                            }
                            //modifica button spunta solo quando un report non è stato ancora creato oppure se la data della visita non è gia passata //#todo ok ma devo controllare anche l'orario
                            else {
                                setGraphic(btn);
                            }
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
