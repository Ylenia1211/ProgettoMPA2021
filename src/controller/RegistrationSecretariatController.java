package controller;

import dao.ConcreteSecretariatDAO;
import datasource.ConnectionDBH2;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Gender;
import model.Secretariat;

import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Implementando i metodi di 'Inizializable' {@link Initializable} inizializza la view associata al controller.
 * Il controller per la registrazione di una nuova segreteria, estende la classe {@link ClientController} e ne eredita i campi della view associata.
 */
public class RegistrationSecretariatController extends ClientController {
    private TextField username;
    private PasswordField password;
    private Label passwordRealTime;
    private Button saveBtn;
    private Label title;
    private final ConcreteSecretariatDAO secretariatRepo;
    private List<TextField> fieldsTextSecretariat;

    /**
     * Il costruttore della classe crea
     * {@link RegistrationSecretariatController#secretariatRepo} un oggetto di tipo {@link ConcreteSecretariatDAO} richiamando la Connessione singleton {@link ConnectionDBH2} del database.
     */
    public RegistrationSecretariatController() {
        this.secretariatRepo = new ConcreteSecretariatDAO(ConnectionDBH2.getInstance());
    }

    /**
     * Setta i campi della view e vi inserisce nuovi elementi
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        super.initialize(url, resourceBundle);

        title = (Label) super.primary_grid.lookup("#labelTitle");
        title.setText("Creazione Utente Segreteria");
        super.pane_main_grid.getChildren().remove(btn); //per rimuovere da pannello dinamicamente il bottone di salvataggio
        this.rbM.setSelected(true); //default
        //username, password
        addFieldUsername();
        addFieldPassword();
        addButtonSave();
        addActionButton();
    }

    /**
     * Aggiunge il {@link TextField} {@link RegistrationSecretariatController#username} alla view della registrazione
     * della segreteria
     */
    public void addFieldUsername() {
        this.username = new TextField();
        this.username.setId("username");
        this.username.setPromptText("Username");
        this.username.setTooltip(new Tooltip("Username"));
        this.username.setStyle("-fx-border-color:#3da4e3; -fx-prompt-text-fill:#163754");
        this.fieldsTextSecretariat = List.of(this.username);
        super.pane_main_grid.getChildren().add(this.username);
    }

    /**
     * Aggiunge il {@link PasswordField} {@link RegistrationSecretariatController#password} alla view della registrazione
     * della segreteria e ne controlla la sicurezza.
     */
    public void addFieldPassword() {
        this.password = new PasswordField();
        this.passwordRealTime = new Label();
        this.password.setTooltip(new Tooltip("Password"));
        this.password.setId("password");
        this.password.setStyle("-fx-border-color:#3da4e3; -fx-prompt-text-fill:#163754");
        this.password.setPromptText("Inserisci password Utente");
        this.password.setOnKeyReleased(e -> {
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

    /**
     * Getter dell'attributo {@link RegistrationSecretariatController#passwordRealTime}
     *
     * @return Il campo {@link RegistrationSecretariatController#passwordRealTime}
     */
    public Label getPasswordRealTime() {
        return passwordRealTime;
    }

    /**
     * Aggiunge il {@link Button} {@link RegistrationSecretariatController#saveBtn} alla view della registrazione della
     * segreteria
     */
    public void addButtonSave() {
        this.saveBtn = new Button("Salva");
        this.saveBtn.setId("saveBtn");
        this.saveBtn.setMaxWidth(MAX_SIZE); //MAX_SIZE
        this.saveBtn.setPrefWidth(Region.USE_COMPUTED_SIZE);
        this.saveBtn.setPrefHeight(30);
        VBox.setMargin(saveBtn, new Insets(0, 100, 0, 100));
        this.saveBtn.setStyle("-fx-background-color: #3DA4E3;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 16px; ");
        super.pane_main_grid.getChildren().add(this.saveBtn);
    }

    /**
     * Getter dell'attributo {@link RegistrationSecretariatController#fieldsTextSecretariat}
     *
     * @return La lista di campi testuali nella view {@link RegistrationSecretariatController#fieldsTextSecretariat}
     */
    public List<TextField> getFieldsTextSecretariat() {
        return fieldsTextSecretariat;
    }

    /**
     * Aggiunge al {@link Button} {@link RegistrationSecretariatController#saveBtn} la funzione per creare una nuova
     * segreteria se non esiste già, solo dopo aver compilato tutti i campi
     */
    public void addActionButton() {
        this.saveBtn.setOnAction(e -> {
            if (!checkEmptyTextField(super.getFieldsText().stream()) &&
                    !checkEmptyTextField(this.fieldsTextSecretariat.stream()) &&
                    !checkAllFieldWithControlRestricted(super.getFieldsControlRestrict().stream()) &&
                    !checkifNotSecurePassword(this.passwordRealTime)) {
                Secretariat secretariat = createSecretariat();
                if (this.secretariatRepo.isNotDuplicate(secretariat)) {
                    try {
                        this.secretariatRepo.add(secretariat);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Impossibile creare l'utente di segreteria! Già esistente!");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Per completare la registrazione devi completare TUTTI i campi!");
            }
        });
    }

    /**
     * Crea un nuovo oggetto di tipo {@link Secretariat} grazie al Builder
     *
     * @return Un oggetto di tipo {@link Secretariat}
     */
    public Secretariat createSecretariat() {
        RadioButton chk = (RadioButton) this.genderGroup.getSelectedToggle();
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
                .addPassword(this.password.getText())
                .build();
    }

    /**
     * Getter dell'attributo {@link RegistrationSecretariatController#username}
     *
     * @return Il campo {@link RegistrationSecretariatController#username}
     */
    public TextField getUsername() {
        return username;
    }

    /**
     * Getter dell'attributo {@link RegistrationSecretariatController#password}
     *
     * @return Il campo {@link RegistrationSecretariatController#password}
     */
    public PasswordField getPassword() {
        return password;
    }

    /**
     * Getter dell'attributo {@link RegistrationSecretariatController#saveBtn}
     *
     * @return Il campo {@link RegistrationSecretariatController#saveBtn}
     */
    public Button getSaveBtn() {
        return saveBtn;
    }

    /**
     * Getter dell'attributo {@link RegistrationSecretariatController#title}
     *
     * @return Il campo {@link RegistrationSecretariatController#title}
     */
    public Label getTitle() {
        return title;
    }

    /**
     * Getter dell'attributo {@link RegistrationSecretariatController#secretariatRepo}
     *
     * @return Il campo {@link RegistrationSecretariatController#secretariatRepo} ovvero un oggetto di tipo {@link ConcreteSecretariatDAO}
     */
    public ConcreteSecretariatDAO getSecretariatRepo() {
        return secretariatRepo;
    }
}
