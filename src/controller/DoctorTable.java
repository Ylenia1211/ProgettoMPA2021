package controller;

import dao.ConcreteDoctorDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Doctor;
import model.Gender;
import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DoctorTable implements Initializable {
    public TableView tableDoctor;
    public TableColumn<Object, Object> col_doctor_id;
    public TableColumn<Object, Object> col_doctor_specialization;
    public TableColumn<Object, Object> col_doctor_username;
    public TableColumn<Object, Object> col_doctor_password;
    private ConcreteDoctorDAO doctorRepo;
    public ObservableList<Doctor> listItems = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableDoctor.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        col_doctor_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_doctor_specialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        col_doctor_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_doctor_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        //#TODO aggiungere listerer onClick Long press quando si clicca su Cliente --> deve spuntare la lista degli animali associati

        try{
            ConnectionDBH2 connection = new ConnectionDBH2();
            doctorRepo = new ConcreteDoctorDAO(connection);
            ResultSet r = doctorRepo.findAll();
            while(r.next()){
                listItems.add(new Doctor.Builder<>(r.getString("specialization"),
                                                   r.getString("username"),
                                                   r.getString("password"))
                        .addSpecialization(r.getString("specialization"))
                        .addUsername(r.getString("username"))
                        .addPassword(r.getString("password"))
                        .build());

                //System.out.println("codice fiscale: " + r.getString("fiscalcode"));
            }
            tableDoctor.setItems(listItems);
            addButtonUpdateToTable();
            addButtonDeleteToTable();

            //listener sulla riga della tabella
            addListenerToTable();


        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    private void addListenerToTable() {
//        this.tableDoctor.setRowFactory( tv -> {
//            TableRow<Doctor> row = new TableRow<>();
//            row.setOnMouseClicked(event -> {
//                if (event.getClickCount() == 1 && (!row.isEmpty()) ) {
//                    Doctor rowData = row.getItem(); // i valori su la row
//                    String idOwnerSearched = doctorRepo.search(rowData);
//                    //System.out.println(rowData.getName());
//                    //System.out.println("id: prima " + idOwnerSearched);
//                    BorderPane borderPane = (BorderPane) this.tableDoctor.getScene().lookup("#borderPane");
//
//                    try {
//                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/showTablePet.fxml"));
//                        loader.setControllerFactory(new Callback<Class<?>, Object>() {
//                            public Object call(Class<?> p) {
//                                return new ShowTablePetController(idOwnerSearched);
//                            }
//                        });
//                        borderPane.setCenter(loader.load());
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            });
//            return row ;
//        });
    }

    private void addButtonUpdateToTable() {
     /*   TableColumn<Doctor, Void> colBtn = new TableColumn<>("");

        Callback<TableColumn<Doctor, Void>, TableCell<Doctor, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Doctor, Void> call(final TableColumn<Doctor, Void> param) {
                return new TableCell<>() {

                    private final Button btn = new Button("Modifica");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Doctor data = getTableView().getItems().get(getIndex());

                            Scene scene = this.getScene();
                            BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registrationClient.fxml"));
                                loader.setControllerFactory(p -> new UpdateDoctorController(data));
                                borderPane.setCenter(loader.load());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ;

                            System.out.println("selectedData: " + data.getId() + " " + data.getName());
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
            }
        };

        colBtn.setCellFactory(cellFactory);

        tableDoctor.getColumns().add(colBtn);*/
    }

    private void addButtonDeleteToTable() {
    /*    TableColumn<Owner, Void> colBtn = new TableColumn<>("");

        Callback<TableColumn<Owner, Void>, TableCell<Owner, Void>> cellFactory = new Callback<TableColumn<Owner, Void>, TableCell<Owner, Void>>() {
            @Override
            public TableCell<Owner, Void> call(final TableColumn<Owner, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Cancella");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Owner data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);
                            JPanel pan = new JPanel();
                            int ok = JOptionPane.showConfirmDialog(
                                    null,
                                    "Sei sicuro di voler cancellare?",
                                    "Cancellazione Cliente",
                                    JOptionPane.YES_NO_OPTION);
                            if (ok == 0) { //cancella
                                String id = doctorRepo.search(data);
                                doctorRepo.delete(id);
                                tableDoctor.getItems().remove(data); //elimina graficamente
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
            }
        };

        colBtn.setCellFactory(cellFactory);
        tableDoctor.getColumns().add(colBtn);
*/
    }
}
