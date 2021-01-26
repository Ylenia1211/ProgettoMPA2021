package controller;

import model.Gender;
import model.Secretariat;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateSecretariatController extends RegistrationSecretariatController {
    private final String id;
    private final Secretariat secretariat;

    public UpdateSecretariatController(Secretariat secretariat) {
        super();
        this.secretariat = secretariat;
        this.id = super.getSecretariatRepo().search(secretariat);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        setParam(secretariat);
    }

    public void setParam(Secretariat data) {
        super.getTitle().setText("Modifica Dati Utente Segreteria");
        super.getTextName().setText(data.getName().trim());
        super.getTextSurname().setText(data.getSurname().trim());
        super.getTextAddress().setText(data.getAddress().trim());
        super.getTextCity().setText(data.getCity().trim());
        super.getTextTelephone().setText(data.getTelephone().trim());
        super.getTextEmail().setText(data.getEmail().trim());
        super.getTextFiscalCode().setText(data.getFiscalCode().trim());
        super.getTextdateBirth().setValue(data.getDatebirth());
        super.getUsername().setText(data.getUsername().trim());
        super.getPassword().setText(data.getPassword().trim());
        if (data.getSex().compareTo(Gender.M) == 0) {
            super.rbM.setSelected(true);
        } else {
            super.rbF.setSelected(true);
        }
    }

    @Override
    public void addActionButton() {
        this.getSaveBtn().setOnAction(e -> {
            if (!checkEmptyTextField(super.getFieldsText().stream()) &&
                    !checkEmptyTextField(super.getFieldsTextSecretariat().stream()) &&
                    !checkAllFieldWithControlRestricted(super.getFieldsControlRestrict().stream()) &&
                    !checkifNotSecurePassword(super.getPasswordRealTime())
            ) {
                Secretariat s = createSecretariat();
                if (super.getSecretariatRepo().isNotDuplicate(s)) {
                    try {
                        super.getSecretariatRepo().update(id, s);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Impossibile creare l'utente di segreteria! Già esistente!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Per completare la registrazione devi completare TUTTI i campi correttamente!");
            }
        });
    }
}
