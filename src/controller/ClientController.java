package controller;


import dao.ConcreteClientDAO;
import datasource.ConnectionDBH2;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.Client;


import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    public TextField textName;
    public TextField textSurname;
    public TextField textAddress;
    public TextField textCity;
    public TextField textTelephone;
    public TextField textEmail;

    //private ClientRepository clientRepo = new ClientRepository();
    private ConcreteClientDAO clientRepo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
             ConnectionDBH2 connection = new ConnectionDBH2();
             clientRepo = new ConcreteClientDAO(connection);
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
                !textEmail.getText().trim().isEmpty()

        ){

            Client p = new Client();
            p.setFirstName(textName.getText());
            p.setLastName(textSurname.getText());
            p.setAddress(textAddress.getText());
            p.setCity(textCity.getText());
            p.setTelephone(textTelephone.getText());
            p.setEmail(textEmail.getText());

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