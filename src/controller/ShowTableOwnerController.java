package controller;

import dao.ConcreteOwnerDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

import model.Gender;
import model.Owner;


import javax.swing.*;
import java.io.IOException;
import java.net.URL;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;
public class ShowTableOwnerController implements Initializable {

    public TableView<Owner> tableClient;
    public TableColumn<Owner, String> col_name;
    public TableColumn<Owner, String> col_surname;
    public TableColumn<Owner, String> col_sex;
    public TableColumn<Owner, String> col_datebirth;
    public TableColumn<Owner, String> col_fiscalCode;
    public TableColumn<Owner, String> col_address;
    public TableColumn<Owner, String> col_city;
    public TableColumn<Owner, String> col_tel;
    public TableColumn<Owner, String> col_email;
    public TableColumn<Owner, String> col_animal;
    private ConcreteOwnerDAO clientRepo;
    public ObservableList<Owner> listItems = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        col_sex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        col_datebirth.setCellValueFactory(new PropertyValueFactory<>("datebirth"));
        col_fiscalCode.setCellValueFactory(new PropertyValueFactory<>("fiscalCode"));  //nome dell'attributo nella classe
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_city.setCellValueFactory(new PropertyValueFactory<>("city"));
        col_tel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_animal.setCellValueFactory(new PropertyValueFactory<>("tot_animal"));

        clientRepo = new ConcreteOwnerDAO(ConnectionDBH2.getInstance());
        listItems.addAll(clientRepo.findAll());
        tableClient.setItems(listItems);
        addButtonUpdateToTable();
        addButtonDeleteToTable();
        //listener sulla riga della tabella
        addListenerToTable();

    }


    private void addListenerToTable() {
        this.tableClient.setRowFactory( tv -> {
            TableRow<Owner> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty()) ) {
                    Owner rowData = row.getItem(); // i valori su la row
                    String idOwnerSearched = clientRepo.search(rowData);
                    //System.out.println(rowData.getName());
                    //System.out.println("id: prima " + idOwnerSearched);
                    BorderPane borderPane = (BorderPane) this.tableClient.getScene().lookup("#borderPane");

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/showTablePet.fxml"));
                        loader.setControllerFactory(new Callback<Class<?>, Object>() {
                            public Object call(Class<?> p) {
                                return new ShowTablePetController(idOwnerSearched);
                            }
                        });
                        borderPane.setCenter(loader.load());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            return row ;
        });
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

                            Scene scene = this.getScene();
                            BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registrationClient.fxml"));
                                loader.setControllerFactory(new Callback<Class<?>, Object>() {
                                    public Object call(Class<?> p) {
                                        return  new UpdateClientController(data);
                                    }
                                });
                                borderPane.setCenter(loader.load());

                            } catch (IOException e) {
                                e.printStackTrace();
                            };

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
