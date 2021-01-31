package controller;

import dao.ConcreteDoctorDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
    private List<TextField> fieldsTextDoctor;
    private List<ComboBox<?>> fieldsComboBox;


    public RegistrationDoctorController() {
         this.doctorRepo = new ConcreteDoctorDAO(ConnectionDBH2.getInstance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        super.initialize(url, resourceBundle);

        title = (Label) super.primary_grid.lookup("#labelTitle");
        title.setText("Creazione Dottore");
        super.pane_main_grid.getChildren().remove(btn); //per rimuovere da pannello dinamicamente il bottone di salvataggio
        this.rbM.setSelected(true); //default

        // menu a tendina per specializzazione
        addFieldSpecialization();
        //username, password
        addFieldUsername();
        addFieldPassword();
        addButtonSave();
        addActionButton();

    }


    public List<TextField> getFieldsTextDoctor() {
        return fieldsTextDoctor;
    }

    public List<ComboBox<?>> getFieldsComboBox() {
        return fieldsComboBox;
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
        this.specialization.setBackground(new Background(new BackgroundFill(Color.valueOf("#94D6F1"), CornerRadii.EMPTY, Insets.EMPTY)));
        this.fieldsComboBox = List.of(this.specialization);
        super.pane_main_grid.getChildren().add(this.specialization);
    }


    public void addFieldUsername()  {
        this.username = new TextField();
        this.username.setId("username");
        this.username.setStyle("-fx-border-color:#3da4e3; -fx-prompt-text-fill:#163754");
        this.username.setPromptText("Username");
        this.username.setTooltip(new Tooltip("Username"));
        this.fieldsTextDoctor = List.of(this.username);
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
                passwordRealTime.setStyle("-fx-text-fill: orange;-fx-font-size: 14px;");
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
        VBox.setMargin(saveBtn, new Insets(0, 100, 0, 100));
        this.saveBtn.setStyle("-fx-background-color: #3da4e3; -fx-text-fill: white;" +
                              "-fx-border-color: transparent; -fx-font-size: 16px;");
        super.pane_main_grid.getChildren().add(this.saveBtn);
        }

    public Label getPasswordRealTime() {
        return passwordRealTime;
    }

    public void addActionButton() {
        //manca il controllo sulla password
            this.saveBtn.setOnAction(e -> {
                if(!checkEmptyTextField(super.getFieldsText().stream()) &&
                   !checkEmptyTextField(this.fieldsTextDoctor.stream()) &&
                   !checkAllFieldWithControlRestricted(super.getFieldsControlRestrict().stream()) &&
                   !checkifNotSecurePassword(this.passwordRealTime) &&
                   !checkEmptyComboBox(this.fieldsComboBox.stream())
                ){
                    Doctor d = createDoctor();
                    if (this.doctorRepo.isNotDuplicate(d)){
                        try {
                            this.doctorRepo.add(d);

                        } catch(Exception ex) {
                            ex.printStackTrace();
                        }
                    } else{
                        JOptionPane.showMessageDialog(null, "Impossibile creare il dottore! Già esistente!");
                    }
               } else{
                    JOptionPane.showMessageDialog(null, "Per completare la registrazione devi completare TUTTI i campi correttamente!");
                }
            });
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
