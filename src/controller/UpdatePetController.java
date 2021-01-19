package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import model.Gender;
import model.Owner;
import model.Pet;

import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class UpdatePetController extends  RegistrationPetController implements Initializable {
    private String id;
    private Pet pet;

    public UpdatePetController(Pet data) {
        super();
        this.pet = data;
        this.id = super.getPetRepo().search(data);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        setParam(pet);
    }

    public static <T, E> String getValueByKey(Map<String, String> map, Object key) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getKey(), key))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
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
    @Override
    public void registrationPet(ActionEvent actionEvent){
        Pet pet = createPet();
        System.out.println(pet.getName());
        System.out.println(pet.getId_owner());
        System.out.println(this.pet.getId_owner());
        if(!(pet.getId_owner().equals(this.pet.getId_owner()))){
            //System.out.println("devo incrementare il contatore");
            this.getPetRepo().addPetToOwner(pet.getId_owner());
            //devo diminuire il contatore dell'altro owner
            this.getPetRepo().dropPetToOwner(this.pet.getId_owner());
           }
        this.getPetRepo().update(this.id, pet);
    }

}