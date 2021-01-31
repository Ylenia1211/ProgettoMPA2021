package controller;

import javafx.event.ActionEvent;
import model.Gender;
import model.Owner;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateClientController extends ClientController{
    private final String id;
    private final Owner own;


    public  UpdateClientController(Owner client) {
        super();
        this.own = client;
        this.id = super.getClientRepo().search(client);
       }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
           super.initialize(url, resourceBundle);
           setParam(own);
    }

    @Override
    public void registerClient(ActionEvent actionEvent){
        if(!checkEmptyTextField(super.getFieldsText().stream()) && !checkAllFieldWithControlRestricted(super.getFieldsControlRestrict().stream())) {
            Owner p = createOwner();
            if (super.getClientRepo().isNotDuplicate(p)) {
                try {
                    super.getClientRepo().update(id, p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Impossibile creare il cliente! Già esistente!");
            }
        }else
        {
            JOptionPane.showMessageDialog(null, "Per completare la registrazione devi completare TUTTI i campi correttamente!!");
        }

    }

    public void setParam(Owner data) {
        super.getTextName().setText(data.getName().trim());
        super.getTextSurname().setText(data.getSurname().trim());
        super.getTextAddress().setText(data.getAddress().trim());
        super.getTextCity().setText(data.getCity().trim());
        super.getTextTelephone().setText(data.getTelephone().trim());
        super.getTextEmail().setText(data.getEmail().trim());
        super.getTextFiscalCode().setText(data.getFiscalCode().trim());
        super.getTextdateBirth().setValue(data.getDatebirth());
        if(data.getSex().compareTo(Gender.M) == 0){
            super.rbM.setSelected(true);
        }else{
            super.rbF.setSelected(true);
        }
    }
}
