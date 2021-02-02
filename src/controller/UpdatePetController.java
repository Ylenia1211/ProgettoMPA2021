package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import model.Doctor;
import model.Gender;
import model.Pet;

import javax.swing.*;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Gestisce l'update dei dati di un paziente, estende la classe {@link RegistrationPetController}
 */
public class UpdatePetController extends  RegistrationPetController implements Initializable {
    private final String id;
    private final Pet pet;

    /**
     * Costruttore della classe, richiama il costruttore della superclasse {@link RegistrationPetController} setta gli
     * attributi {@link UpdatePetController#pet} e {@link UpdatePetController#id}
     *
     * @param data Il dottore
     */
    public UpdatePetController(Pet data) {
        super();
        this.pet = data;
        this.id = super.getPetRepo().search(data);
    }

    /**
     * Richiama la funzione {@link RegistrationPetController#initialize(URL, ResourceBundle)} della superclasse e
     * setta i parametri grazie alla funzione {@link UpdatePetController#setParam(Pet)}
     *
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        setParam(pet);
    }

    /**
     * #Todo: da rivedere
     * @param map
     * @param key
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> String getValueByKey(Map<String, String> map, Object key) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getKey(), key))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * Inserisce i dati del paziente negli appositi campi
     *
     * @param data L'oggetto di tipo {@link Pet}
     */
    public void setParam(Pet data) {
        String ownerCode = getValueByKey(super.getListClient(), data.getId_owner().trim());
        super.getTextName().setText(data.getName().trim());
        super.getTextSurname().setText(data.getSurname().trim());
        super.getTextParticularSign().setText(data.getParticularSign().trim());
        super.getTextPetRace().setValue(data.getId_petRace().trim());
        super.getSearchText().setText(ownerCode); // qua bisogna fare una ricerca ValueByKey
        super.getTextdateBirth().setValue(data.getDatebirth());
        if(data.getSex().compareTo(Gender.M) == 0){
            super.rbM.setSelected(true);
        }else{
            super.rbF.setSelected(true);
        }
    }

    /**
     * Verifica se non si stia inserendo un utente già registrato e crea un nuovo proprietario solo se tutti i campi
     * sono stati compilati correttamente
     *
     * @param actionEvent L'evento registrato, in questo caso il click sul bottone
     */
    @Override
    public void registrationPet(ActionEvent actionEvent){
        if (checkSearchFieldIsCorrect(super.getListClient().values(), super.getSearchText().getText()) && !checkEmptyTextField(super.getFieldsTextPet().stream()) && !checkEmptyComboBox(super.getFieldsComboBox().stream())) {
        Pet pet = createPet();
        if (this.getPetRepo().isNotDuplicate(pet)) {
            try {
                if(!(pet.getId_owner().equals(this.pet.getId_owner()))){
                    //System.out.println("devo incrementare il contatore");
                    this.getPetRepo().addPetToOwner(pet.getId_owner());
                    //devo diminuire il contatore dell'altro owner
                    this.getPetRepo().dropPetToOwner(this.pet.getId_owner());
                }
                this.getPetRepo().update(this.id, pet);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Impossibile creare Animale! Già esistente!");
        }
    }else{
            JOptionPane.showMessageDialog(null, "Per completare la registrazione devi completare TUTTI i campi!");
        }
    }
}
