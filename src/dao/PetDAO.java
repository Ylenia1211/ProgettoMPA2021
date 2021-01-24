package dao;

import model.Pet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PetDAO extends Crud<Pet> {
    List<String> searchAllRace();
    Map<String, String> searchAllClientByFiscalCod();
    List<Pet> searchByOwner(String id);
    String search(Pet pet);
    boolean isNotDuplicate(Pet pet); //fa controlli piu stretti rispetto al search
    void addPetToOwner(String id_owner); //+1 pet sul contatore tot_animal di owner
    void dropPetToOwner(String id_owner);//-1 pet sul contatore tot_animal di owner
}
