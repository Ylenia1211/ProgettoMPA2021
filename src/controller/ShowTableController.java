package controller;

import dao.ConcreteOwnerDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import model.Doctor;
import model.Owner;


import javax.swing.*;
import java.io.IOException;
import java.net.URL;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;
public class ShowTableController  implements Initializable {

    public TableView<Owner> tableClient;
    public TableColumn<Owner, String> col_name;
    public TableColumn<Owner, String> col_surname;
    public TableColumn<Owner, String> col_sex;
    public TableColumn<Owner, String> col_datebirth;
    public TableColumn<Owner, String> col_address;
    public TableColumn<Owner, String> col_city;
    public TableColumn<Owner, String> col_tel;
    public TableColumn<Owner, String> col_email;
    private ConcreteOwnerDAO clientRepo;
    public ObservableList<Owner> listItems = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableClient.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        col_sex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        col_datebirth.setCellValueFactory(new PropertyValueFactory<>("datebirth"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_city.setCellValueFactory(new PropertyValueFactory<>("city"));
        col_tel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));

        try{
            ConnectionDBH2 connection = new ConnectionDBH2();
            clientRepo = new ConcreteOwnerDAO(connection);
            ResultSet r =  clientRepo.findAll();
            while(r.next()){
                listItems.add( new Owner.Builder<>()
                    .addName(r.getString("name"))
                    .addSurname(r.getString("surname"))
                        .addSex(r.getString("sex"))
                        .addDateBirth( LocalDate.parse(r.getString("datebirth")))
                         .addAddress(r.getString("address"))
                         .addCity(r.getString("city"))
                         .addTelephone(r.getString("telephone"))
                          .addEmail(r.getString("email")).build()
                        );
            }
            tableClient.setItems(listItems);
            addButtonUpdateToTable();
            addButtonDeleteToTable();
        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    private void addButtonUpdateToTable() {
        TableColumn<Owner, Void> colBtn = new TableColumn("");

        Callback<TableColumn<Owner, Void>, TableCell<Owner, Void>> cellFactory = new Callback<TableColumn<Owner, Void>, TableCell<Owner, Void>>() {
            @Override
            public TableCell<Owner, Void> call(final TableColumn<Owner, Void> param) {
                final TableCell<Owner, Void> cell = new TableCell<Owner, Void>() {

                    private final Button btn = new Button("Modifica");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Owner data = getTableView().getItems().get(getIndex());
                           //#TODO: far apparire vista di update in tab
                           ///FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registrationClient.fxml"));
                            /*
                            try {

                                Parent changeView = loader.load();
                                ClientController controller = loader.getController();
                                //UpdateClientController upd = (UpdateClientController) controller;
                                controller.setParam(data);
                                Scene sceneUpdate = new Scene(changeView);

                                Stage stage1 = new  Stage();
                                stage1.setScene(sceneUpdate);
                                stage1.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                              */
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
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        tableClient.getColumns().add(colBtn);
    }



    private void addButtonDeleteToTable() {
        TableColumn<Owner, Void> colBtn = new TableColumn("");

        Callback<TableColumn<Owner, Void>, TableCell<Owner, Void>> cellFactory = new Callback<TableColumn<Owner, Void>, TableCell<Owner, Void>>() {
            @Override
            public TableCell<Owner, Void> call(final TableColumn<Owner, Void> param) {
                final TableCell<Owner, Void> cell = new TableCell<Owner, Void>() {
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
                            if(ok ==0) { //cancella
                                String id = clientRepo.search(data);
                                clientRepo.delete(id);
                                tableClient.getItems().remove(data); //elimina graficamente
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

        tableClient.getColumns().add(colBtn);

    }
}

