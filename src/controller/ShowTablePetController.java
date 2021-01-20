package controller;

import dao.ConcretePetDAO;
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
import model.Pet;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static util.gui.ButtonTable.addButtonDeleteToTable;
import static util.gui.ButtonTable.addButtonUpdateToTable;

public class ShowTablePetController  implements Initializable {
    public TableColumn<Pet, String> col_name;
    public TableColumn<Pet, String> col_surname;
    public TableColumn<Pet, String> col_sex;
    public TableColumn<Pet, String> col_datebirth;
    public TableColumn<Pet, String> col_type;
    public TableColumn<Pet, String> col_particularSign;
    public TableView<Pet> tableSpecificPets;
    private ConcretePetDAO petRepo;
    public ObservableList<Pet> listItems = FXCollections.observableArrayList();
    private final String id_owner;

    public ShowTablePetController(String idOwner) {
        this.id_owner = idOwner;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableSpecificPets.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        col_sex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        col_datebirth.setCellValueFactory(new PropertyValueFactory<>("datebirth"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("id_petRace"));  //nome dell'attributo nella classe
        col_particularSign.setCellValueFactory(new PropertyValueFactory<>("particularSign"));


        petRepo = new ConcretePetDAO(ConnectionDBH2.getInstance());
        //System.out.println("id: dopo " + this.id_owner);
        List<Pet> petsResult = petRepo.searchByOwner(this.id_owner);
        //petsResult.stream().forEach(x -> listItems.add(x));
        listItems.addAll(petsResult); //scrittura piu compatta
        tableSpecificPets.setItems(listItems);

        var colBtnUpdate =  addButtonUpdateToTable("/view/registrationPet.fxml", tableSpecificPets, -1);
        tableSpecificPets.getColumns().add((TableColumn<Pet, ?>) colBtnUpdate);
        var colBtnDelete = addButtonDeleteToTable(tableSpecificPets, Pet.class);
        tableSpecificPets.getColumns().add((TableColumn<Pet, ?>)colBtnDelete);
    }



}
