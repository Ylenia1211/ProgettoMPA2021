package controller;

import dao.ConcreteOwnerDAO;
import dao.ConcretePetDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Gender;
import model.Owner;
import model.Pet;

import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

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
    private String id_owner;

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

        try{
            //ConnectionDBH2 connection = new ConnectionDBH2();
            petRepo = new ConcretePetDAO(ConnectionDBH2.getInstance());
            System.out.println("id: dopo " + this.id_owner);
            List<Pet> petsResult = petRepo.searchByOwner(this.id_owner);

            //petsResult.stream().forEach(x -> listItems.add(x));
            listItems.addAll(petsResult); //scrittura piu compatta
            tableSpecificPets.setItems(listItems);
        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }
}
