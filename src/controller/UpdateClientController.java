package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Owner;
import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateClientController extends ClientController{
    private String id;
    private Owner own;


    public  UpdateClientController(Owner client) {
        super();
        this.own = client;
        this.id = super.getClientRepo().search(client);
       }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
           super.initialize(url, resourceBundle);

           //addTextField();
           setParam(own);
           //System.out.println("Name " + own.getName());
    }

    @Override
    public void registerClient(ActionEvent actionEvent){
        JOptionPane.showMessageDialog(null, "Cliente Modificato!");
        //JOptionPane.showMessageDialog(null, this.getTextName().getText());
        Owner p = createOwner();
        super.getClientRepo().update(id, p);
    }

    public void setParam(Owner data) {

        super.getTextName().setText(data.getName().trim());
        super.getTextSurname().setText(data.getSurname().trim());
        super.getTextAddress().setText(data.getAddress().trim());
        super.getTextCity().setText(data.getCity().trim());
        super.getTextTelephone().setText(data.getCity().trim());
        super.getTextEmail().setText(data.getEmail().trim());
        super.getTextdateBirth().setValue(data.getDatebirth());
        if(data.getSex().equals("M")){
            super.rbM.setSelected(true);
        }else{
            super.rbF.setSelected(true);
        }
    }

    //aggingere campi gui dinamicamente
    private void addTextField()  {
        //super.pane_main_grid.getChildren().remove(btn); per rimuovere da pannello dinamicamente
        TextField newField = new TextField("NUOVO CAMPO");
        super.pane_main_grid.getChildren().add(newField);
    }
    /*
    @FXML
    @Override
    protected void initialize() {
        super.initialize();
    }*/
}
