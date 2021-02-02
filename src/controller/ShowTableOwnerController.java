package controller;
import dao.ConcreteOwnerDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import model.Owner;
import util.gui.ButtonTable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Gestisce la tabella con i dati dei proprietari
 */
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

    /**
     * Genera la tabella con tutti gli attributi dei proprietari e vi aggiunge bottoni per modificarli e cancellarli e
     * aggiunge un listener alla tabella.
     *
     * {@inheritDoc}
     */
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
        var colBtnUpdate = ButtonTable.addButtonUpdateToTable("/view/registrationClient.fxml", 2);
        tableClient.getColumns().add((TableColumn<Owner, ?>) colBtnUpdate);
        var colBtnDelete = ButtonTable.addButtonDeleteToTable(tableClient, Owner.class);
        tableClient.getColumns().add((TableColumn<Owner, ?>)colBtnDelete);
        //listener sulla riga della tabella
        addListenerToTable();
    }

    /**
     * Aggiunge alla tabella {@link ShowTableOwnerController#tableClient} un listener che permette, cliccando sulla riga
     * di un proprietario, un redirect verso una tabella contenente i pazienti ad esso collegati
     */
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

