package controller;


import dao.ConcreteOwnerDAO;
import datasource.ConnectionDBH2;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.FieldVerifier;
import model.Gender;
import model.Owner;
import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable, FieldVerifier {
    @FXML
    public VBox pane_main_grid;
    @FXML
    public Label labelTitle;
    @FXML
    public TextField textFiscalCode;
    @FXML
    private final TextField textName;
    @FXML
    private final TextField textSurname;
    @FXML
    private final TextField textAddress;
    @FXML
    private final TextField textCity;
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
    //private ClientRepository clientRepo = new ClientRepository();
    private ConcreteOwnerDAO clientRepo;


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

        try{
            //ConnectionDBH2 connection = new ConnectionDBH2();
            this.clientRepo = new ConcreteOwnerDAO(ConnectionDBH2.getInstance());
        }

        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.textFiscalCode.textProperty().addListener((observableFC, oldValueFC, newValueFC) -> {
            if (!FieldVerifier.super.fiscalCodeVerifier(newValueFC))
                this.textFiscalCode.setStyle("-fx-border-color: red");
            else
                this.textFiscalCode.setStyle("-fx-border-color: lightgreen");
        });
//        this.textTelephone.setTextFormatter(new TextFormatter<>(change ->
//                (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));
        this.textTelephone.textProperty().addListener((observableT, oldValueT, newValueT) -> {
            if (!FieldVerifier.super.phoneNumberVerifier(newValueT))
                this.textTelephone.setStyle("-fx-border-color: red");
            else
                this.textTelephone.setStyle("-fx-border-color: lightgreen");
        });
        this.textEmail.textProperty().addListener((observableE, oldValueE, newValueE) -> {
            if (!FieldVerifier.super.emailVerifier(newValueE))
                this.textEmail.setStyle("-fx-border-color: red");
            else
                this.textEmail.setStyle("-fx-border-color: lightgreen");
        });
//        this.textFiscalCode.setTextFormatter(new TextFormatter<>(change ->
//                (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));

    }

    //onAction="#registerClient"
    public void registerClient(ActionEvent actionEvent) {
        if(!this.textName.getText().trim().isEmpty() &&
                !this.textSurname.getText().trim().isEmpty() &&
                !this.textAddress.getText().trim().isEmpty() &&
                !this.textCity.getText().trim().isEmpty() &&
                !this.textFiscalCode.getText().trim().isEmpty() &&
                !this.textTelephone.getText().trim().isEmpty() &&
                !this.textEmail.getText().trim().isEmpty() &&
                (this.rbM.isSelected() || this.rbF.isSelected()))
        {
            Owner p = createOwner();

            try {
                clientRepo.add(p);
                this. textName.clear();
                this.textSurname.clear();
                this.textAddress.clear();
                this.textCity.clear();
                this.textFiscalCode.clear();
                this.textTelephone.clear();
                this.textEmail.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Owner createOwner(){
        RadioButton chk = (RadioButton)this.genderGroup.getSelectedToggle();
        System.out.println(chk.getText());

        return new Owner.Builder<>()
                .addName(this.textName.getText())
                .addSurname(this.textSurname.getText())
                .addSex((chk.getText().equals("M") ? Gender.M : Gender.F))
                .addDateBirth(this.textdateBirth.getValue())
                .addFiscalCode(this.textFiscalCode.getText())
                .addAddress(this.textAddress.getText())
                .addCity(this.textCity.getText())
                .addTelephone(this.textTelephone.getText())
                .addEmail(this.textEmail.getText())
                .setTotAnimal(0)
                .build();
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