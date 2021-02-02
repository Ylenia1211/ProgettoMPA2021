package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import model.Gender;
import model.Owner;

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
 * Gestisce l'update dei dati di un proprietario, estende la classe {@link ClientController} ereditandone i campi della view associata
 */
public class UpdateClientController extends ClientController {
    private final String id;
    private final Owner own;

    /**
     * Il costruttore della classe, assegna a {@link UpdateClientController#own} il proprietario passato a parametro, ed
     * a {@link UpdateClientController#id} l'id del proprietario usando la funzione della superclasse
     * {@link ClientController#getClientRepo()}
     *
     * @param client Il proprietario
     */
    public UpdateClientController(Owner client) {
        super();
        this.own = client;
        this.id = super.getClientRepo().search(client);
    }

    /**
     * Richiama la funzione {@link ClientController#initialize(URL, ResourceBundle)} della superclasse e setta i
     * parametri della view grazie alla funzione {@link UpdateClientController#setParam(Owner)}
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        setParam(own);
    }

    /**
     * Verifica se non si stia inserendo un utente già registrato e crea un nuovo proprietario solo se tutti i campi
     * sono stati compilati correttamente
     *
     * @param actionEvent L'evento registrato, in questo caso il click sul bottone
     */
    @Override
    public void registerClient(ActionEvent actionEvent) {
        if (!checkEmptyTextField(super.getFieldsText().stream()) && !checkAllFieldWithControlRestricted(super.getFieldsControlRestrict().stream())) {
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
        } else {
            JOptionPane.showMessageDialog(null, "Per completare la registrazione devi completare TUTTI i campi correttamente!!");
        }

    }

    /**
     * Inserisce i dati del proprietario negli appositi campi
     *
     * @param data L'oggetto di tipo {@link Owner}
     */
    public void setParam(Owner data) {
        super.getTextName().setText(data.getName().trim());
        super.getTextSurname().setText(data.getSurname().trim());
        super.getTextAddress().setText(data.getAddress().trim());
        super.getTextCity().setText(data.getCity().trim());
        super.getTextTelephone().setText(data.getTelephone().trim());
        super.getTextEmail().setText(data.getEmail().trim());
        super.getTextFiscalCode().setText(data.getFiscalCode().trim());
        super.getTextdateBirth().setValue(data.getDatebirth());
        if (data.getSex().compareTo(Gender.M) == 0) {
            super.rbM.setSelected(true);
        } else {
            super.rbF.setSelected(true);
        }
    }
}
