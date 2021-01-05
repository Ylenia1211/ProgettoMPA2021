package controller;


import dao.ConcreteOwnerDAO;
import datasource.ConnectionDBH2;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.Gender;
import model.Owner;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    public TextField textName;
    public TextField textSurname;
    public TextField textAddress;
    public TextField textCity;
    public TextField textTelephone;
    public TextField textEmail;
    public DatePicker textdateBirth;
    public Button btn;
    public RadioButton rbM;
    public RadioButton rbF;
    public HBox gender;
    public ToggleGroup genderGroup;
    //private ClientRepository clientRepo = new ClientRepository();
    private ConcreteOwnerDAO clientRepo;

    public ClientController() {
        rbM = new RadioButton(Gender.M.getDeclaringClass().descriptorString());
        rbF = new RadioButton(Gender.F.getDeclaringClass().descriptorString());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            ConnectionDBH2 connection = new ConnectionDBH2();
            clientRepo = new ConcreteOwnerDAO(connection);

        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }


    public void registerClient(ActionEvent actionEvent) {
        if(!textName.getText().trim().isEmpty() &&
                !textSurname.getText().trim().isEmpty() &&
                !textAddress.getText().trim().isEmpty() &&
                !textCity.getText().trim().isEmpty() &&
                !textTelephone.getText().trim().isEmpty() &&
                !textEmail.getText().trim().isEmpty() &&
                (rbM.isSelected() || rbF.isSelected()))
        {
            Owner p = new Owner.Builder<>()
                    .addName(textName.getText())
                    .addSurname(textSurname.getText())
                    .addSex(String.valueOf(genderGroup.getSelectedToggle().toString().equals("M") ? Gender.M : Gender.F))
                    .addDateBirth(textdateBirth.getValue())
                    .addAddress(textAddress.getText())
                    .addCity(textCity.getText())
                    .addTelephone(textTelephone.getText())
                    .addEmail(textEmail.getText())
                    .build();

            try {
                clientRepo.add(p);
                textName.clear();
                textSurname.clear();
                textAddress.clear();
                textCity.clear();
                textTelephone.clear();
                textEmail.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }



        }

    }
}