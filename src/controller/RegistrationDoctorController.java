package controller;

import dao.ConcreteDoctorDAO;
import dao.ConcreteOwnerDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import model.Doctor;
import model.Gender;
import model.Owner;

import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

//uso la stessa view di registrazione dell'owner, registrationClient.fxml
public class RegistrationDoctorController extends ClientController{
    private TextField username;
    private PasswordField password;
    private ComboBox<String> specialization;
    private Button saveBtn;
    private Label title;
    private ConcreteDoctorDAO doctorRepo;

    public RegistrationDoctorController() {
         this.doctorRepo = new ConcreteDoctorDAO(ConnectionDBH2.getInstance());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        super.initialize(url, resourceBundle);

        title = (Label) super.pane_main_grid.lookup("#labelTitle");
        title.setText("Creazione Dottore");
        super.pane_main_grid.getChildren().remove(btn); //per rimuovere da pannello dinamicamente il bottone di salvataggio
        // menu a tendina per specializzazione
        addFieldSpecialization();
        //username, password
        addFieldUsername();
        addFieldPassword();
        addButtonSave();
        addActionButton();
    }
    public void addFieldSpecialization()  {
        List<String> listSpecialization = this.getDoctorRepo().searchAllSpecialization();
        this.specialization = new ComboBox(FXCollections
                .observableArrayList(listSpecialization));
        this.specialization.setId("specialization");
        this.specialization.setPromptText("Scegli specializzazione");
        super.pane_main_grid.getChildren().add(this.specialization);
    }


    public void addFieldUsername()  {
        this.username = new TextField();
        this.username.setId("username");
        this.username.setPromptText("Username");
        super.pane_main_grid.getChildren().add(this.username);
    }

    public  void addFieldPassword()  {
        this.password = new PasswordField();
        this.password.setId("password");
        this.password.setPromptText("Inserisci password Utente");
        super.pane_main_grid.getChildren().add(this.password);
    }

    public  void addButtonSave()  {
        this.saveBtn = new Button("Salva");
        this.saveBtn.setId("saveBtn");
        this.saveBtn.setPrefWidth(200);
        this.saveBtn.setPrefHeight(30);
        this.saveBtn.setStyle("-fx-background-color: #3DA4E3;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 16px; ");
        super.pane_main_grid.getChildren().add(this.saveBtn);
        }

    public void addActionButton() {
            this.saveBtn.setOnAction(e -> {
                Doctor d = createDoctor();
                //inserire controlli
                this.doctorRepo.add(d);
            });

    }

    public ConcreteDoctorDAO getDoctorRepo() {
        return doctorRepo;
    }

    public Doctor createDoctor(){
        RadioButton chk = (RadioButton)this.genderGroup.getSelectedToggle();
        return new Doctor.Builder<>()
                .addName(super.getTextName().getText())
                .addSurname(super.getTextSurname().getText())
                .addSex((chk.getText().equals("M") ? Gender.M : Gender.F)) //toString()
                .addDateBirth(super.getTextdateBirth().getValue())
                .addAddress(super.getTextAddress().getText())
                .addCity(super.getTextCity().getText())
                .addTelephone(super.getTextTelephone().getText())
                .addEmail(super.getTextEmail().getText())
                .addFiscalCode(super.getTextFiscalCode().getText())
                .addSpecialization((String) this.specialization.getValue())
                .addUsername(this.username.getText())
                .addPassword( this.password.getText())
                .build();
    }
    public TextField getUsername() {
        return username;
    }

    public PasswordField getPassword() {
        return password;
    }

    public ComboBox<String> getSpecialization() {
        return specialization;
    }

    public Button getSaveBtn() {
        return saveBtn;
    }

    public Label getTitle() {
        return title;
    }
}
