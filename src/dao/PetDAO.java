package dao;

import model.Pet;

import java.util.List;

public interface PetDAO extends Crud<Pet> {
    List<String> searchAllRace();
    List<String> searchAllSurnameClient();  //da cambiare
}
