package controller;

import javafx.fxml.Initializable;
import model.Gender;
import model.Secretariat;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Implementando i metodi di 'Inizializable' {@link Initializable} inizializza la view associata al controller.
 * Gestisce l'update dei dati di una segretaria, estende la classe {@link RegistrationSecretariatController} ereditandone i campi della view associata
 */
public class UpdateSecretariatController extends RegistrationSecretariatController {
    private final String id;
    private final Secretariat secretariat;

    /**
     * Costruttore della classe, richiama il costruttore della superclasse {@link RegistrationSecretariatController} setta gli
     * attributi {@link UpdateSecretariatController#secretariat} e {@link UpdateSecretariatController#id} usando la funzione della superclasse
     * {@link RegistrationSecretariatController#getSecretariatRepo()}
     *
     * @param secretariat l'utente della segreteria
     */
    public UpdateSecretariatController(Secretariat secretariat) {
        super();
        this.secretariat = secretariat;
        this.id = super.getSecretariatRepo().search(secretariat);
    }

    /**
     * Richiama la funzione {@link RegistrationSecretariatController#initialize(URL, ResourceBundle)} della superclasse e
     * setta i parametri grazie alla funzione {@link UpdateSecretariatController#setParam(Secretariat)}
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        setParam(secretariat);
    }

    /**
     * Setta i dati della segreteria negli appositi campi della view
     *
     * @param data L'oggetto di tipo {@link Secretariat}
     */
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

    /**
     * Assegna al bottone della superclasse saveBtn la funzione di verificare se non si stia inserendo una segreteria già
     * registrata e crea un nuova segreteria solo se tutti i campi sono stati compilati correttamente
     */
    @Override
    public void addActionButton() {
        this.getSaveBtn().setOnAction(e -> {
            if (!checkEmptyTextField(super.getFieldsText().stream()) &&
                    !checkEmptyTextField(super.getFieldsTextSecretariat().stream()) &&
                    !checkAllFieldWithControlRestricted(super.getFieldsControlRestrict().stream()) &&
                    !checkifNotSecurePassword(super.getPasswordRealTime())
            ) {
                Secretariat s = createSecretariat();

                if(!secretariat.getPassword().equals(s.getPassword())) {
                    super.getSecretariatRepo().updatePassword(id, s.getPassword());
                }

                if (super.getSecretariatRepo().isNotDuplicate(s)) {
                    try {
                        super.getSecretariatRepo().update(id, s);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Per completare la registrazione devi completare TUTTI i campi correttamente!");
            }
        });
    }
}
