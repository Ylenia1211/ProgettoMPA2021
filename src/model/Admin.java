package model;
/**
 *
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 *
 * Classe utilizzata per creare un oggetto 'Admin':{@link Admin}
 */
public class Admin {
    private String id;
    private String username;
    private String password;

    public Admin() {
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
