package model;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per creare un oggetto 'Admin':{@link Admin}
 */
public class Admin {
    private String id;
    private String username;
    private String password;

    /**
     * Metodo costruttore default
     */
    public Admin() { }

    /**
     * Metodo che restituisce l'id di un Admin
     *
     * @return id di oggetto Admin
     */
    public String getId() {
        return id;
    }

    /**
     * Metodo che setta l'username di un Admin
     *
     * @param username da settare nell' oggetto Admin
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Metodo che setta la password di un Admin
     *
     * @param password da settare nell' oggetto Admin
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Metodo che restituisce l'username di un Admin
     *
     * @return username di oggetto Admin
     */
    public String getUsername() {
        return username;
    }

    /**
     * Metodo che restituisce la password di un Admin
     *
     * @return password di oggetto Admin
     */
    public String getPassword() {
        return password;
    }
}
