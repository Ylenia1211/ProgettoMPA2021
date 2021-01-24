package controller;
import dao.ConcreteSecretariatDAO;
import datasource.ConnectionDBH2;
import javafx.scene.control.*;
import model.Doctor;
import model.Gender;
import model.Secretariat;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationSecretariatController extends ClientController{
    private TextField username;
    private PasswordField password;
    private Button saveBtn;
    private Label title;
    private final ConcreteSecretariatDAO secretariatRepo;

    public RegistrationSecretariatController() {
        this.secretariatRepo = new ConcreteSecretariatDAO(ConnectionDBH2.getInstance());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        super.initialize(url, resourceBundle);

        title = (Label) super.pane_main_grid.lookup("#labelTitle");
        title.setText("Creazione Utente Segreteria");
        super.pane_main_grid.getChildren().remove(btn); //per rimuovere da pannello dinamicamente il bottone di salvataggio

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
       /* this.saveBtn.setOnAction(e -> {
            System.out.println("inserire utente segreteria");
            Secretariat secretariat = createSecretariat();
            //inserire controlli
            this.secretariatRepo.add(secretariat);
        });*/
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
                    !this.username.getText().trim().isEmpty() &&
                    !this.password.getText().trim().isEmpty()
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
                .addName(super.getTextName().getText())
                .addSurname(super.getTextSurname().getText())
                .addSex((chk.getText().equals("M") ? Gender.M : Gender.F))
                .addDateBirth(super.getTextdateBirth().getValue())
                .addAddress(super.getTextAddress().getText())
                .addCity(super.getTextCity().getText())
                .addTelephone(super.getTextTelephone().getText())
                .addEmail(super.getTextEmail().getText())
                .addFiscalCode(super.getTextFiscalCode().getText())
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
