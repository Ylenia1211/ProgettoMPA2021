package model;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per rappresentare un oggetto 'Doctor':{@link Doctor} estende la classe astratta 'Person':{@link Person}
 */
public class Doctor extends Person {

    /**
     * Classe utilizzata per creare un oggetto 'Doctor':{@link Doctor} estende la classe astratta 'Builder' di Person:{@link model.Person.Builder}
     */
    public static class Builder<T extends Doctor.Builder<T>> extends Person.Builder<Doctor.Builder<T>> {

        protected String specialization;
        protected String username;
        protected String password;

        /**
         * Metodo costruttore default del Builder, che richiama i metodi costruttori delle classi Parent: {@link Person.Builder}, {@link MasterData.Builder}.
         */
        public Builder() {
            super();
        }

        /**
         * Metodo costruttore che richiama i metodi costruttori delle classi Parent: {@link Person.Builder}, {@link MasterData.Builder}.
         *
         * @param specialization setta il campo specializzazione del dottore da creare
         * @param username       setta il campo username del dottore da creare
         * @param password       setta il campo password del dottore da creare
         */
        public Builder(String specialization, String username, String password) {
            super();
            this.specialization = specialization;
            this.username = username;
            this.password = password;
        }

        /**
         * Metodo che permette l'inserimento del campo 'specialization' all'interno di Doctor.
         *
         * @param specialization specializzazione da aggiungere al Doctor.
         * @return Builder per la costruzione dell'oggetto Doctor.
         */
        public Builder addSpecialization(String specialization) {
            this.specialization = specialization;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'username' all'interno di Doctor.
         *
         * @param username username da aggiungere al Doctor.
         * @return Builder per la costruzione dell'oggetto Doctor.
         */
        public Builder addUsername(String username) {
            this.username = username;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'password' all'interno di Doctor.
         *
         * @param password password da aggiungere al Doctor.
         * @return Builder per la costruzione dell'oggetto Doctor.
         */
        public Builder addPassword(String password) {
            this.password = password;
            return this;
        }

        /**
         * Metodo che restituisce un oggetto di tipo {@link Doctor.Builder}
         *
         * @return Doctor.Builder
         */
        @Override
        public Doctor.Builder getThis() {
            return this;
        }

        /**
         * Metodo che effettuata la costruzione di un nuovo Doctor e lo restituisce
         *
         * @return Doctor creato con i metodi del Builder {@link Doctor.Builder}
         */
        public Doctor build() {
            return new Doctor(this);
        }
    }

    final private String specialization;
    final private String username;
    final private String password;

    /**
     * Metodo costruttore di un oggetto Doctor tramite l'oggetto Builder creato
     *
     * @param builder oggetto Builder {@link Doctor.Builder}
     */
    public Doctor(Builder builder) {
        super(builder);
        this.specialization = builder.specialization;
        this.username = builder.username;
        this.password = builder.password;
    }

    /**
     * Metodo che restituisce la specializzazione di un Doctor
     *
     * @return la specializzazione di un Doctor
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Metodo che restituisce l'username di un Doctor
     *
     * @return l'username di un Doctor
     */
    public String getUsername() {
        return username;
    }

    /**
     * Metodo che restituisce la password di un Doctor
     *
     * @return la password di un Doctor
     */
    public String getPassword() {
        return password;
    }
}

