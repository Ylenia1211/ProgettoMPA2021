package controller;
import dao.ConcreteDoctorDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Doctor;
import java.net.URL;
import java.util.ResourceBundle;
import static util.gui.ButtonTable.addButtonDeleteToTable;
import static util.gui.ButtonTable.addButtonUpdateToTable;

public class ShowTableDoctorController implements Initializable {
    public TableView<Doctor> tableDoctor;
    public TableColumn<Doctor, String> col_name;
    public TableColumn<Doctor, String> col_surname;
    public TableColumn<Doctor, String> col_sex;
    public TableColumn<Doctor, String> col_datebirth;
    public TableColumn<Doctor, String> col_fiscalCode;
    public TableColumn<Doctor, String> col_address;
    public TableColumn<Doctor, String> col_city;
    public TableColumn<Doctor, String> col_tel;
    public TableColumn<Doctor, String> col_email;
    public TableColumn<Doctor, String> col_specialization;
    public TableColumn<Doctor, String> col_username;
    public TableColumn<Doctor, String> col_password;
    private final ConcreteDoctorDAO doctorRepo;
    public ObservableList<Doctor> listItems = FXCollections.observableArrayList();

    public ShowTableDoctorController() {
        this.doctorRepo = new ConcreteDoctorDAO(ConnectionDBH2.getInstance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableDoctor.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        col_sex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        col_datebirth.setCellValueFactory(new PropertyValueFactory<>("datebirth"));
        col_fiscalCode.setCellValueFactory(new PropertyValueFactory<>("fiscalCode"));  //nome dell'attributo nella classe
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_city.setCellValueFactory(new PropertyValueFactory<>("city"));
        col_tel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_specialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_password.setCellValueFactory(new PropertyValueFactory<>("password"));

        listItems.addAll(this.doctorRepo.findAll());
        tableDoctor.setItems(listItems);
        var colBtnUpdate =  addButtonUpdateToTable("/view/registrationClient.fxml", tableDoctor, 0);
        tableDoctor.getColumns().add((TableColumn<Doctor, ?>) colBtnUpdate);
        var colBtnDelete = addButtonDeleteToTable(tableDoctor, Doctor.class);
        tableDoctor.getColumns().add((TableColumn<Doctor, ?>)colBtnDelete);
    }

 }

