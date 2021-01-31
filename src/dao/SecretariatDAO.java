package dao;

import model.Secretariat;
import model.User;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Interfaccia Data Access Object per il tipo di oggetto {@link Secretariat} estende l'interfaccia generica {@link Crud}.
 * Dichiara i metodi specifici per un oggetto di tipo Secretariat {@link Secretariat}.
 */
public interface SecretariatDAO extends Crud<Secretariat> {
    String search(Secretariat data);

    Secretariat searchByUsernameAndPassword(User user);

    boolean isNotDuplicate(Secretariat data); //fa controlli piu stretti rispetto al search

    Secretariat searchById(String id);

    void updatePassword(String id, String pwd);
}
