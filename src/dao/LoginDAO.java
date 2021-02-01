package dao;

import model.User;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Interfaccia Data Access Object per il tipo di oggetto {@link User}.
 * Dichiara i metodi specifici per un oggetto di tipo User {@link User}.
 */
public interface LoginDAO {
    boolean searchUser(User userLogged);

    boolean searchUserNamePasswordAdmin(User userLogged);

    boolean searchUserNamePasswordSecretariat(User userLogged);

    boolean searchUserNamePasswordDoctor(User userLogged);
}
