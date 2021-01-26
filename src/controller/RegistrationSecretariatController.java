package controller;
import dao.ConcreteSecretariatDAO;
import datasource.ConnectionDBH2;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import model.Gender;
import model.Secretariat;

import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RegistrationSecretariatController extends ClientController{
    private TextField username;
    private PasswordField password;
    private Label passwordRealTime;
    private Button saveBtn;
    private Label title;
    private final ConcreteSecretariatDAO secretariatRepo;
    private List<TextField> fieldsTextSecretariat;

    public RegistrationSecretariatController() {
        this.secretariatRepo = new ConcreteSecretariatDAO(ConnectionDBH2.getInstance());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        super.initialize(url, resourceBundle);

        title = (Label) super.pane_main_grid.lookup("#labelTitle");
        title.setText("Creazione Utente Segreteria");
        super.pane_main_grid.getChildren().remove(btn); //per rimuovere da pannello dinamicamente il bottone di salvataggio
        this.rbM.setSelected(true); //default
        //username, password
        addFieldUsername();
        addFieldPassword();
        addButtonSave();
        addActionButton();
    }

    public void addFieldUsername()  {
        this.username = new TextField();
        this.username.setId("username");
        this.username.setPromptText("Username");
        this.username.setTooltip(new Tooltip("Username"));
        this.username.setStyle("-fx-border-color:#3da4e3; -fx-prompt-text-fill:#163754");
        this.fieldsTextSecretariat = List.of(this.username);
        super.pane_main_grid.getChildren().add(this.username);
    }


    public  void addFieldPassword()  {
        this.password = new PasswordField();
        this.passwordRealTime = new Label();
        this.password.setTooltip(new Tooltip("Password"));
        this.password.setId("password");
        this.password.setStyle("-fx-border-color:#3da4e3; -fx-prompt-text-fill:#163754");
        this.password.setPromptText("Inserisci password Utente");
        this.password.setOnKeyReleased( e-> {
            String checkPassword = this.password.getText();
            System.out.println(checkPassword);
            if (checkPassword.length() < 6) {
                passwordRealTime.setText("Password poco sicura");
                passwordRealTime.setStyle("-fx-text-fill: red;-fx-font-size: 14px;");
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

    public Label getPasswordRealTime() {
        return passwordRealTime;
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

    public List<TextField> getFieldsTextSecretariat() {
        return fieldsTextSecretariat;
    }

    public void addActionButton() {
        this.saveBtn.setOnAction(e -> {
            if(!checkEmptyTextField(super.getFieldsText().stream()) &&
                    !checkEmptyTextField(this.fieldsTextSecretariat.stream()) &&
                    !checkAllFieldWithControlRestricted(super.getFieldsControlRestrict().stream()) &&
                    !checkifNotSecurePassword(this.passwordRealTime)
            )
            {
                Secretariat secretariat = createSecretariat();
                if(this.secretariatRepo.isNotDuplicate(secretariat)){
                    try {
                        this.secretariatRepo.add(secretariat);

                    }catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }else {
                    JOptionPane.showMessageDialog(null, "Impossibile creare l'utente di segreteria! Già esistente!");
                }

            }else
            {
                JOptionPane.showMessageDialog(null, "Per completare la registrazione devi completare TUTTI i campi!");
            }});
    }

    public Secretariat createSecretariat(){
        RadioButton chk = (RadioButton)this.genderGroup.getSelectedToggle();
        return new Secretariat.Builder<>()
                .addName(super.getTextName().getText().toUpperCase())
                .addSurname(super.getTextSurname().getText().toUpperCase())
                .addSex((chk.getText().equals("M") ? Gender.M : Gender.F))
                .addDateBirth(super.getTextdateBirth().getValue())
                .addAddress(super.getTextAddress().getText().toUpperCase())
                .addCity(super.getTextCity().getText().toUpperCase())
                .addTelephone(super.getTextTelephone().getText())
                .addEmail(super.getTextEmail().getText())
                .addFiscalCode(super.getTextFiscalCode().getText().toUpperCase())
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

    public Button getSaveBtn() {
        return saveBtn;
    }

    public Label getTitle() {
        return title;
    }

    public ConcreteSecretariatDAO getSecretariatRepo() {
        return secretariatRepo;
    }
}
