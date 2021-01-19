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
}
