package controller;

import dao.ConcreteAppointmentDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import model.Appointment;
import util.FieldVerifier;
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
        addButtonViewInfoOwnerPet();
        addButtonCreateReport();
        addButtonViewReport();
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

    private void addButtonViewReport() {
        TableColumn<Appointment, Void> colBtn = new TableColumn("");
        Callback<TableColumn<Appointment, Void>, TableCell<Appointment, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Appointment, Void> call(final TableColumn<Appointment, Void> param) {
                final TableCell<Appointment, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Report");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Appointment data = getTableView().getItems().get(getIndex());
                            //System.out.println("Print idOwner prenotazione" + data.getId_owner());
                            Scene scene = this.getScene();
                            BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/createReport.fxml"));
                                loader.setControllerFactory(p -> new CreateReportController(data, true));
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
                            if (appointmentRepo.searchIfExistAppointmentInReport(id_appointment)) {
                                setGraphic(btn);
                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        tableAllBookingVisit.getColumns().add(colBtn);
    }

    private void addButtonCreateReport() {

        Callback<TableColumn<Appointment, Void>, TableCell<Appointment, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Appointment, Void> call(final TableColumn<Appointment, Void> param) {
                final TableCell<Appointment, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Crea Report");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Appointment data = getTableView().getItems().get(getIndex());
                            //System.out.println("Print idOwner prenotazione" + data.getId_owner());
                            Scene scene = this.getScene();
                            BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/createReport.fxml"));
                                loader.setControllerFactory(new Callback<Class<?>, Object>() {
                                    public Object call(Class<?> p) {
                                        return new CreateReportController(data, false);
                                    }
                                });
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
                            if (appointmentRepo.searchIfExistAppointmentInReport(id_appointment)) {
                                setGraphic(null);
                            }
                            //posso creare il report solo se la data della visita è precedente alla data di oggi  // data di oggi +1
                            else if (getTableColumn().getTableView().getItems().get(getIndex()).getLocalDate().isBefore(LocalDate.now().plusDays(1))) {
                                setGraphic(btn);
                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                };
                return cell;
            }
        };
        colBtnCreateReport.setCellFactory(cellFactory);
        tableAllBookingVisit.getColumns().add(colBtnCreateReport);
    }

    private void addButtonViewInfoOwnerPet() {
        TableColumn<Appointment, Void> colBtn = new TableColumn("");
        Callback<TableColumn<Appointment, Void>, TableCell<Appointment, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Appointment, Void> call(final TableColumn<Appointment, Void> param) {
                final TableCell<Appointment, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Dettagli");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Appointment data = getTableView().getItems().get(getIndex());
                            Scene scene = this.getScene();
                            BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/showInfoOwnerPet.fxml"));
                                loader.setControllerFactory(p -> new ShowInfoOwnerPetController(data));
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
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        tableAllBookingVisit.getColumns().add(colBtn);
    }

}
