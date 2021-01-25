package controller;

import dao.ConcreteDoctorDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import model.Doctor;
import model.Gender;

import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

//uso la stessa view di registrazione dell'owner, registrationClient.fxml
public class RegistrationDoctorController extends ClientController{
    private TextField username;
    private PasswordField password;
    private Label passwordRealTime;
    private ComboBox<String> specialization;
    private Button saveBtn;
    private Label title;
    private final ConcreteDoctorDAO doctorRepo;

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
        this.specialization = new ComboBox<>(FXCollections
                .observableArrayList(listSpecialization));
        this.specialization.setId("specialization");
        this.specialization.setPromptText("Scegli specializzazione");
        this.specialization.setTooltip(new Tooltip("Specializzazione in un tipo di visita"));
        this.specialization.setMaxWidth(MAX_SIZE);
        this.specialization.setPrefWidth(Region.USE_COMPUTED_SIZE);
        super.pane_main_grid.getChildren().add(this.specialization);
    }


    public void addFieldUsername()  {
        this.username = new TextField();
        this.username.setId("username");
        this.username.setStyle("-fx-border-color:#3da4e3; -fx-prompt-text-fill:#163754");
        this.username.setPromptText("Username");
        this.username.setTooltip(new Tooltip("Username"));
        super.pane_main_grid.getChildren().add(this.username);
    }
    //private TextField passwordRealtime;
    public  void addFieldPassword()  {
        this.passwordRealTime = new Label();
        this.password = new PasswordField();
        this.password.setStyle("-fx-border-color:#3da4e3; -fx-prompt-text-fill:#163754");
        this.password.setTooltip(new Tooltip("Password"));
        this.password.setId("password");
        this.password.setPromptText("Inserisci password Utente");
        this.password.setOnKeyReleased( e-> {
            String checkPassword = this.password.getText();
            System.out.println(checkPassword);
            if (checkPassword.length() < 6) {
                passwordRealTime.setText("Password poco sicura");
                passwordRealTime.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            } else {
                passwordRealTime.setText("Password mediamente sicura");
                passwordRealTime.setStyle("-fx-text-fill: #b0511a;-fx-font-size: 14px;");
            }
            if (checkPassword.length() >= 6 && checkPassword.matches(".*\\d.*")) {
                passwordRealTime.setText("Password molto sicura");
                passwordRealTime.setStyle("-fx-text-fill: green;-fx-font-size: 14px;");
            }

        });
        super.pane_main_grid.getChildren().add(this.password);
        super.pane_main_grid.getChildren().add(this.passwordRealTime);
    }

    public  void addButtonSave()  {
        this.saveBtn = new Button("Salva");
        this.saveBtn.setId("saveBtn");
        this.saveBtn.setMaxWidth(MAX_SIZE); //MAX_SIZE
        this.saveBtn.setPrefWidth(Region.USE_COMPUTED_SIZE);
        this.saveBtn.setPrefHeight(30);
        this.saveBtn.setStyle("-fx-background-color: #3DA4E3;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 16px; ");
        super.pane_main_grid.getChildren().add(this.saveBtn);
        }

    public void addActionButton() {

           //controlli
            this.saveBtn.setOnAction(e -> {
                if(!super.getTextName().getText().trim().isEmpty() &&
                        !super.getTextSurname().getText().trim().isEmpty() &&
                        !super.getTextAddress().getText().trim().isEmpty() &&
                        !super.getTextCity().getText().trim().isEmpty() &&
                        !super.getTextFiscalCode().getText().trim().isEmpty() &&
                        !super.getTextTelephone().getText().trim().isEmpty() &&
                        !super.getTextEmail().getText().trim().isEmpty() &&
                        (this.rbM.isSelected() || rbF.isSelected()) &&
                        !this.specialization.getValue().trim().isEmpty() &&
                        !this.username.getText().trim().isEmpty() &&
                        !this.password.getText().trim().isEmpty()
                )
                {
                Doctor d = createDoctor();
                if(this.doctorRepo.isNotDuplicate(d)){
                    try {
                        this.doctorRepo.add(d);

                    }catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }else {
                    JOptionPane.showMessageDialog(null, "Impossibile creare il dottore! Già esistente!");
                }

               }else
                {
                    JOptionPane.showMessageDialog(null, "Per completare la registrazione devi completare TUTTI i campi!");
                }});
    }

    public ConcreteDoctorDAO getDoctorRepo() {
        return doctorRepo;
    }

    public Doctor createDoctor(){
        RadioButton chk = (RadioButton)this.genderGroup.getSelectedToggle();
        return new Doctor.Builder<>()
                .addName(super.getTextName().getText().toUpperCase())
                .addSurname(super.getTextSurname().getText().toUpperCase())
                .addSex((chk.getText().equals("M") ? Gender.M : Gender.F)) //toString()
                .addDateBirth(super.getTextdateBirth().getValue())
                .addAddress(super.getTextAddress().getText().toUpperCase())
                .addCity(super.getTextCity().getText().toUpperCase())
                .addTelephone(super.getTextTelephone().getText())
                .addEmail(super.getTextEmail().getText())
                .addFiscalCode(super.getTextFiscalCode().getText().toUpperCase())
                .addSpecialization(this.specialization.getValue().toUpperCase())
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
