package dao;

import model.Pet;

import java.util.List;
import java.util.Map;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Interfaccia Data Access Object per il tipo di oggetto {@link Pet} estende l'interfaccia generica {@link Crud}.
 * Dichiara i metodi specifici per un oggetto di tipo Pet {@link Pet}.
 */
public interface PetDAO extends Crud<Pet> {
    List<String> searchAllRace();

    Map<String, String> searchAllClientByFiscalCod();

    List<Pet> searchByOwner(String id);

    String search(Pet pet);

    boolean isNotDuplicate(Pet pet); //fa controlli piu stretti rispetto al search

    void addPetToOwner(String id_owner); //+1 pet sul contatore tot_animal di owner

    void dropPetToOwner(String id_owner);//-1 pet sul contatore tot_animal di owner
}
