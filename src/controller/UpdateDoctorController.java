package controller;

import javafx.fxml.Initializable;
import model.Doctor;
import model.Gender;

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
 * Gestisce l'update dei dati di un dottore, estende la classe {@link RegistrationDoctorController}  ereditandone i campi della view associata
 */
public class UpdateDoctorController extends RegistrationDoctorController{
    private final String id;
    private final Doctor doctor;

    /**
     * Costruttore della classe, richiama il costruttore della superclasse {@link RegistrationDoctorController} setta gli
     * attributi {@link UpdateDoctorController#doctor} e {@link UpdateDoctorController#id} usando la funzione della superclasse
     * {@link RegistrationDoctorController#getDoctorRepo()}
     *
     * @param doctor Il dottore
     */
    public UpdateDoctorController(Doctor doctor) {
        super();
        this.doctor = doctor;
        this.id = super.getDoctorRepo().search(doctor);
    }

    /**
     * Richiama la funzione {@link RegistrationDoctorController#initialize(URL, ResourceBundle)} della superclasse e
     * setta i parametri grazie alla funzione {@link UpdateDoctorController#setParam(Doctor)}
     *
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        setParam(doctor);
    }

    /**
     * Inserisce i dati del dottore negli appositi campi
     *
     * @param data L'oggetto di tipo {@link Doctor}
     */
    public void setParam(Doctor data) {
        super.getTitle().setText("Modifica Dati Dottore");
        super.getTextName().setText(data.getName().trim());
        super.getTextSurname().setText(data.getSurname().trim());
        super.getTextAddress().setText(data.getAddress().trim());
        super.getTextCity().setText(data.getCity().trim());
        super.getTextTelephone().setText(data.getTelephone().trim());
        super.getTextEmail().setText(data.getEmail().trim());
        super.getTextFiscalCode().setText(data.getFiscalCode().trim());
        super.getTextdateBirth().setValue(data.getDatebirth());
        super.getSpecialization().setValue(data.getSpecialization().trim());
        super.getUsername().setText(data.getUsername().trim());
        super.getPassword().setText(data.getPassword().trim());
        if(data.getSex().compareTo(Gender.M) == 0){
            super.rbM.setSelected(true);
        }else{
            super.rbF.setSelected(true);
        }
    }

    /**
     * Assegna al bottone della superclasse saveBtn la funzione di verificare se non si stia inserendo un dottore già
     * registrato e crea un nuovo dottore solo se tutti i campi sono stati compilati correttamente
     */
    @Override
    public void addActionButton() {
       this.getSaveBtn().setOnAction(e -> {
           if (!checkEmptyTextField(super.getFieldsText().stream()) &&
               !checkEmptyTextField(super.getFieldsTextDoctor().stream()) &&
               !checkAllFieldWithControlRestricted(super.getFieldsControlRestrict().stream()) &&
               !checkifNotSecurePassword(super.getPasswordRealTime()))
           {
               Doctor d = createDoctor();
               if (super.getDoctorRepo().isNotDuplicate(d)) {
                   try {
                       super.getDoctorRepo().update(id, d);
                   } catch (Exception ex) {
                       ex.printStackTrace();
                   }
               } else {
                   JOptionPane.showMessageDialog(null, "Impossibile creare il dottore! Già esistente!");
               }
           }else {
               JOptionPane.showMessageDialog(null, "Per completare la registrazione devi completare TUTTI i campi correttamente!");
           }
       });
   }
}
