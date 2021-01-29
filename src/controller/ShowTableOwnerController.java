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
import javafx.util.Callback;
import model.Owner;
import util.gui.ButtonTable;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
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
        var colBtnUpdate = ButtonTable.addButtonUpdateToTable("/view/registrationClient.fxml", tableClient, 2);
        tableClient.getColumns().add((TableColumn<Owner, ?>) colBtnUpdate);
        var colBtnDelete = ButtonTable.addButtonDeleteToTable(tableClient, Owner.class);
        tableClient.getColumns().add((TableColumn<Owner, ?>)colBtnDelete);
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
                    BorderPane borderPane = (BorderPane) this.tableClient.getScene().lookup("#borderPane");
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/showTablePet.fxml"));
                        loader.setControllerFactory(p -> new ShowTablePetController(idOwnerSearched));
                        borderPane.setCenter(loader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });
    }
}

