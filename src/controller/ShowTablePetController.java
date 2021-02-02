package controller;

import dao.ConcreteDoctorDAO;
import dao.ConcretePetDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Pet;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import static util.gui.ButtonTable.addButtonDeleteToTable;
import static util.gui.ButtonTable.addButtonUpdateToTable;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Gestisce la tabella con i dati dei pazienti
 */
public class ShowTablePetController  implements Initializable {
    public TableColumn<Pet, String> col_name;
    public TableColumn<Pet, String> col_surname;
    public TableColumn<Pet, String> col_sex;
    public TableColumn<Pet, String> col_datebirth;
    public TableColumn<Pet, String> col_type;
    public TableColumn<Pet, String> col_particularSign;
    public TableView<Pet> tableSpecificPets;
    private final  ConcretePetDAO petRepo;
    public ObservableList<Pet> listItems = FXCollections.observableArrayList();
    private final String id_owner;

    /**
     * Il costruttore della classe, assegna a {@link ShowTablePetController#id_owner} l'id del proprietario passato a
     * parametro, ed a {@link ShowTablePetController#id_owner} una nuova istanza di {@link ConcretePetDAO}
     * @param idOwner
     */
    public ShowTablePetController(String idOwner) {
        this.id_owner = idOwner;
        this.petRepo = new ConcretePetDAO(ConnectionDBH2.getInstance());
    }

    /**
     * Genera la tabella con tutti gli attributi dei pazienti e vi aggiunge bottoni per modificarli e cancellarli
     *
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableSpecificPets.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        col_sex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        col_datebirth.setCellValueFactory(new PropertyValueFactory<>("datebirth"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("id_petRace"));  //nome dell'attributo nella classe
        col_particularSign.setCellValueFactory(new PropertyValueFactory<>("particularSign"));

        List<Pet> petsResult = this.petRepo.searchByOwner(this.id_owner);
        //petsResult.stream().forEach(x -> listItems.add(x));
        listItems.addAll(petsResult); //scrittura piu compatta
        tableSpecificPets.setItems(listItems);

        var colBtnUpdate =  addButtonUpdateToTable("/view/registrationPet.fxml", tableSpecificPets, -1);
        tableSpecificPets.getColumns().add((TableColumn<Pet, ?>) colBtnUpdate);
        var colBtnDelete = addButtonDeleteToTable(tableSpecificPets, Pet.class);
        tableSpecificPets.getColumns().add((TableColumn<Pet, ?>)colBtnDelete);
    }
}
