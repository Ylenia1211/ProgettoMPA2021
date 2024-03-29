package controller;

import dao.ConcreteSecretariatDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Secretariat;

import java.net.URL;
import java.util.ResourceBundle;

import static util.gui.ButtonTable.addButtonDeleteToTable;
import static util.gui.ButtonTable.addButtonUpdateToTable;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Implementando i metodi di 'Inizializable' {@link Initializable} inizializza la view associata al controller.
 * Gestisce la tabella con i dati della segreteria
 */
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

    /**
     * Il costruttore della classe, assegna all'attributo {@link ShowTableSecretariatController#secretariatRepo} una nuova istanza di
     * {@link ConcreteSecretariatDAO}  richiamando la Connessione singleton {@link ConnectionDBH2} del database.
     */
    public ShowTableSecretariatController() {
        this.secretariatRepo = new ConcreteSecretariatDAO(ConnectionDBH2.getInstance());
    }

    /**
     * Inizializza la tabella con tutti gli attributi della segreteria e vi aggiunge bottoni per modificarla e cancellarla
     *
     * {@inheritDoc}
     */
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

        var colBtnUpdate =  addButtonUpdateToTable("/view/registrationClient.fxml", 1);
        tableSecretariat.getColumns().add((TableColumn<Secretariat, ?>) colBtnUpdate);

        var colBtnDelete = addButtonDeleteToTable(tableSecretariat, Secretariat.class);
        tableSecretariat.getColumns().add((TableColumn<Secretariat, ?>)colBtnDelete);
    }
}
