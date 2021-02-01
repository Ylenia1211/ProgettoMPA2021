package model;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per rappresentare un oggetto 'User':{@link User}
 */
public class User {

    private final String username;
    private final String password;
    private final String role;

    /**
     * Metodo costruttore di un oggetto User tramite l'oggetto Builder creato
     *
     * @param builder oggetto Builder {@link User.Builder}
     */
    public User(Builder builder) {
        username = builder.username;
        password = builder.password;
        role = builder.role;
    }

    /**
     * Metodo che restituisce l'username di un Utente
     *
     * @return username di un Utente
     */
    public String getUsername() {
        return username;
    }

    /**
     * Metodo che restituisce la password di un Utente
     *
     * @return password di un Utente
     */
    public String getPassword() {
        return password;
    }

    /**
     * Metodo che restituisce il ruolo all'interno del sistema, di un Utente
     *
     * @return ruolo, all'interno del sistema, dell'Utente
     */
    public String getRole() {
        return role;
    }

    /**
     * Metodo per la stampa l'oggetto User
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    /**
     * Classe utilizzata per creare un oggetto 'User':{@link User}
     */
    public static class Builder {
        private String username;
        private String password;
        private String role;

        /**
         * Metodo costruttore default del Builder.
         */
        public Builder() {
        }

        /**
         * Metodo che permette l'inserimento del campo 'username' all'interno del User.
         *
         * @param username setta l'username del User.
         * @return Builder per la costruzione dell'oggetto User.
         */
        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'password' all'interno del User.
         *
         * @param password setta la password del User.
         * @return Builder per la costruzione dell'oggetto User.
         */
        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'role' all'interno del User.
         *
         * @param role setta il ruolo dell' User.
         * @return Builder per la costruzione dell'oggetto User.
         */
        public Builder setRole(String role) {
            this.role = role;
            return this;
        }

        /**
         * Metodo che effettuata la costruzione di un nuovo User lo restituisce
         *
         * @return User creato con i metodi del Builder {@link User.Builder}
         */
        public User build() {
            return new User(this);
        }
    }

}
