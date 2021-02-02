package controller.factorySidebar;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Gestisce il controllo della sidebar, personalizzandola in base al tipo di utente loggato, grazie al design pattern
 * Factory
 */
public class SideBarFactory {

    /**
     * Gestisce il controllo della sidebar, personalizzandola in base al tipo di utente loggato.
     *
     * @param roleUser La stringa con l'indicazione della tipologia di utente (il ruolo).
     * @return un oggetto di tipo 'SideBarAction' personalizzato.
     */
    public static SideBarAction createSideBar(String roleUser) {
        switch (roleUser) {
            case "Dottore" -> {
                return new DoctorSideBar();
            }
            case "Amministratore" -> {
                return new AdminSideBar();
            }
            case "Segreteria" -> {
                return new SecretariatSideBar();
            }
            default -> throw new IllegalStateException("Unexpected value: " + roleUser);
        }
    }
}


