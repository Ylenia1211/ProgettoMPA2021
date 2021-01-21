package controller;

import dao.ConcreteDoctorDAO;
import dao.ConcreteSecretariatDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Doctor;
import model.Secretariat;

import java.net.URL;
import java.util.ResourceBundle;

import static util.gui.ButtonTable.addButtonDeleteToTable;
import static util.gui.ButtonTable.addButtonUpdateToTable;

public class ShowTableSecretariatController implements Initializable {
    public TableView<Secretariat> tableSecretariat;
    public TableColumn<Secretariat, String> col_name;
    public TableColumn<Secretariat, String> col_surname;
    public TableColumn<Secretariat, String> col_sex;
    public TableColumn<Secretariat, String> col_datebirth;
    public TableColumn<Secretariat, String> col_fiscalCode;
    public TableColumn<Secretariat, String> col_address;
    public TableColumn<Secretariat, String> col_city;
    public TableColumn<Secretariat, String> col_tel;
    public TableColumn<Secretariat, String> col_email;
    public TableColumn<Secretariat, String> col_username;
    public TableColumn<Secretariat, String> col_password;
    private final ConcreteSecretariatDAO secretariatRepo;
    public ObservableList<Secretariat> listItems = FXCollections.observableArrayList();

    public ShowTableSecretariatController() {
        this.secretariatRepo = new ConcreteSecretariatDAO(ConnectionDBH2.getInstance());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableSecretariat.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        col_sex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        col_datebirth.setCellValueFactory(new PropertyValueFactory<>("datebirth"));
        col_fiscalCode.setCellValueFactory(new PropertyValueFactory<>("fiscalCode"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_city.setCellValueFactory(new PropertyValueFactory<>("city"));
        col_tel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        listItems.addAll(this.secretariatRepo.findAll());
        tableSecretariat.setItems(listItems);
        //#todo: implementare update controller

        var colBtnUpdate =  addButtonUpdateToTable("/view/registrationClient.fxml", tableSecretariat, 1);
        tableSecretariat.getColumns().add((TableColumn<Secretariat, ?>) colBtnUpdate);

        var colBtnDelete = addButtonDeleteToTable(tableSecretariat, Secretariat.class);
        tableSecretariat.getColumns().add((TableColumn<Secretariat, ?>)colBtnDelete);
    }
}
