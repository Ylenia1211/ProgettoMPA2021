package controller;
import dao.ConcreteOwnerDAO;
import datasource.ConnectionDBH2;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import util.gui.Common;
import util.FieldVerifier;
import model.Gender;
import model.Owner;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable, FieldVerifier {
    @FXML
    public VBox pane_main_grid;
    @FXML
    public Label labelTitle;
    @FXML
    public TextField textFiscalCode;
    @FXML
    private TextField textName;
    @FXML
    private TextField textSurname;
    @FXML
    private TextField textAddress;
    @FXML
    private TextField textCity;
    @FXML
    public TextField textTelephone;
    @FXML
    public TextField textEmail;
    @FXML
    public DatePicker textdateBirth;
    @FXML
    public Button btn;
    public RadioButton rbM;
    public RadioButton rbF;
    public HBox gender;
    public ToggleGroup genderGroup;
    public double MAX_SIZE = 1.7976931348623157E308;
    private final ConcreteOwnerDAO clientRepo;
    private List<TextField> fieldsText;
    private List<TextField> fieldsControlRestrict;


    public ClientController() {
        this.rbM = new RadioButton(Gender.M.getDeclaringClass().descriptorString());
        this.rbF = new RadioButton(Gender.F.getDeclaringClass().descriptorString());
        this.textName = new TextField();
        this.textSurname = new TextField();
        this.textAddress = new TextField();
        this.textCity = new TextField();
        this.textTelephone = new TextField();
        this.textEmail = new TextField();
        this.textdateBirth = new DatePicker();
        this.textFiscalCode = new TextField();

        this.clientRepo = new ConcreteOwnerDAO(ConnectionDBH2.getInstance());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTooltipOnField(); //serve a mettere le etichette su i textfield quando ci si passa sopra con il mouse


        this.btn.setMaxWidth(MAX_SIZE); //MAX_SIZE
        this.btn.setPrefWidth(Region.USE_COMPUTED_SIZE);
        this.btn.setStyle("-fx-background-color: #3DA4E3;-fx-text-fill: white;" +
                " -fx-border-color: transparent; -fx-font-size: 14px; ");
        this.rbM.setSelected(true);


        int yearsValid = LocalDate.now().getYear() - 18; //posso registrare solo chi ha almeno 18 anni e non piu di 90 anni
        Common.restrictDatePicker(this.textdateBirth, LocalDate.of(1930, 1,1), LocalDate.of(yearsValid,1,1));
        this.textdateBirth.setValue( LocalDate.of(1980, 1,1));
        this.textdateBirth.setEditable(false);

        setListenerCriticalFields(this.textFiscalCode, this.textTelephone, this.textEmail);

        this.fieldsText = List.of(this.textName,
                this.textSurname,
                this.textAddress,
                this.textCity,
                this.textFiscalCode,
                this.textTelephone,
                this.textEmail);
        this.fieldsControlRestrict = List.of(this.textFiscalCode,
                this.textTelephone,
                this.textEmail);
    }


    private void setTooltipOnField() {
        this.textName.setTooltip(new Tooltip("Nome"));
        this.textSurname.setTooltip(new Tooltip("Cognome"));
        this.textAddress.setTooltip(new Tooltip("Indirizzo"));
        this.textCity.setTooltip(new Tooltip("Città"));
        this.textFiscalCode.setTooltip(new Tooltip("Codice Fiscale"));
        this.textTelephone.setTooltip(new Tooltip("Telefono"));
        this.textEmail.setTooltip(new Tooltip("Email"));
    }

    public void registerClient(ActionEvent actionEvent) {

        if(!checkEmptyTextField(this.fieldsText.stream()) && !checkAllFieldWithControlRestricted(this.fieldsControlRestrict.stream()))
        {
            Owner p = createOwner();
            if(this.clientRepo.isNotDuplicate(p)){
                try {
                    clientRepo.add(p);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                JOptionPane.showMessageDialog(null, "Impossibile creare il cliente! Già esistente!");
            }

        }else
        {
            JOptionPane.showMessageDialog(null, "Per completare la registrazione devi completare TUTTI i campi correttamente!");
        }
    }

    public Owner createOwner(){
        RadioButton chk = (RadioButton)this.genderGroup.getSelectedToggle();
        System.out.println(chk.getText());

        return new Owner.Builder<>()
                .addName(this.textName.getText().toUpperCase())
                .addSurname(this.textSurname.getText().toUpperCase())
                .addSex((chk.getText().equals("M") ? Gender.M : Gender.F))
                .addDateBirth(this.textdateBirth.getValue())
                .addFiscalCode(this.textFiscalCode.getText().toUpperCase())
                .addAddress(this.textAddress.getText().toUpperCase())
                .addCity(this.textCity.getText().toUpperCase())
                .addTelephone(this.textTelephone.getText())
                .addEmail(this.textEmail.getText())
                .setTotAnimal(0)
                .build();
    }

    public List<TextField> getFieldsText() {
        return fieldsText;
    }

    public List<TextField> getFieldsControlRestrict() {
        return fieldsControlRestrict;
    }

    public ConcreteOwnerDAO getClientRepo() {
        return this.clientRepo;
    }

    public TextField getTextName() {
        return this.textName;
    }

    public TextField getTextSurname() {
        return textSurname;
    }

    public TextField getTextAddress() {
        return textAddress;
    }

    public TextField getTextCity() {
        return textCity;
    }

    public TextField getTextTelephone() {
        return textTelephone;
    }

    public TextField getTextEmail() {
        return textEmail;
    }

    public DatePicker getTextdateBirth() {
        return textdateBirth;
    }

    public TextField getTextFiscalCode() {
        return textFiscalCode;
    }
}