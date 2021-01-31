package dao;

import model.Doctor;
import model.User;

import java.util.List;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Interfaccia Data Access Object per il tipo di oggetto {@link Doctor} estende l'interfaccia generica {@link Crud}.
 * Dichiara i metodi specifici per un oggetto di tipo Doctor {@link Doctor}.
 */
public interface DoctorDAO extends Crud<Doctor> {
    List<String> searchAllSpecialization();

    String search(Doctor data);

    Doctor searchByUsernameAndPassword(User user);

    Doctor searchById(String id);

    boolean isNotDuplicate(Doctor doctor); //fa controlli più stretti rispetto al search

    void updatePassword(String id, String pwd);
}
