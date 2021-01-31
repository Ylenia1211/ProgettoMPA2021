package dao;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Interfaccia Data Access Object per il tipo di oggetto {@link model.Admin} estende l'interfaccia generica {@link Crud}.
 * Dichiara i metodi specifici per un oggetto di tipo Admin {@link model.Admin}.
 */
public interface AdminDAO {
    String searchEmailClinic();

    String searchPasswordClinic();

    void updatePasswordClinic(String psw);

    void updateEmailClinic(String email);
}
