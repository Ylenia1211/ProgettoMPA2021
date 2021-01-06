package controller;

import dao.ConcreteOwnerDAO;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.Gender;
import model.Owner;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateClientController extends ClientController implements Initializable{
    //private ConcreteOwnerDAO clientRepo;
    private String id;
    private Owner own;


    public  UpdateClientController(Owner client) {
        super();
        this.own = client;
        this.id = super.getClientRepo().search(client);
        //System.out.println(super.textSurname.getText().isEmpty());
        //this.setParam(client);
       }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
           setParam(own);
           System.out.println("Name " + own.getName());
    }

    @Override
    public void registerClient(ActionEvent actionEvent){
        JOptionPane.showMessageDialog(null, "HEY MODIFICAA");
        JOptionPane.showMessageDialog(null, this.getTextName().getText());
        Owner p = createOwner();
        super.getClientRepo().update(id, p);
    }

    public void setParam(Owner data) {

        super.getTextName().setText(data.getName().trim());
        super.getTextSurname().setText(data.getSurname().trim());
        //#TODO altri campi


    }
}
