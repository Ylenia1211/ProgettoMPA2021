package model;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per rappresentare un oggetto 'Secretariat':{@link Secretariat} estende la classe astratta 'Person':{@link Person}
 */
public class Secretariat extends Person {
    /**
     * Classe utilizzata per creare un oggetto 'Secretariat':{@link Secretariat} estende la classe astratta 'Builder' di Person:{@link model.Person.Builder}
     */
    public static class Builder<T extends Secretariat> extends Person.Builder<Secretariat.Builder<T>> {


        protected String username;
        protected String password;

        /**
         * Metodo costruttore default del Builder, che richiama i metodi costruttori delle classi Parent: {@link Person.Builder}, {@link MasterData.Builder}.
         */
        public Builder() {
            super();
        }

        /**
         * Metodo che permette l'inserimento del campo 'username' all'interno di Secretariat.
         *
         * @param username username da aggiungere al Secretariat.
         * @return Builder per la costruzione dell'oggetto Secretariat.
         */
        public Builder addUsername(String username) {
            this.username = username;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'password' all'interno di Secretariat.
         *
         * @param password username da aggiungere al Secretariat.
         * @return Builder per la costruzione dell'oggetto Secretariat.
         */
        public Builder addPassword(String password) {
            this.password = password;
            return this;
        }

        /**
         * Metodo che restituisce un oggetto di tipo {@link Secretariat.Builder}
         *
         * @return Secretariat.Builder
         */
        @Override
        public Secretariat.Builder getThis() {
            return this;
        }

        /**
         * Metodo che effettuata la costruzione di un nuovo Secretariat e lo restituisce
         *
         * @return Secretariat creato con i metodi del Builder {@link Secretariat.Builder}
         */
        public Secretariat build() {
            return new Secretariat(this);
        }
    }


    final private String username;
    final private String password;

    /**
     * Metodo costruttore di un oggetto Secretariat tramite l'oggetto Builder creato
     *
     * @param builder oggetto Builder {@link Secretariat.Builder}
     */
    protected Secretariat(Builder builder) {
        super(builder);
        this.username = builder.username;
        this.password = builder.password;
    }

    /**
     * Metodo che restituisce l'username di un Secretariat
     *
     * @return l'username di un Secretariat
     */
    public String getUsername() {
        return username;
    }

    /**
     * Metodo che restituisce la password di un Secretariat
     *
     * @return la password di un Secretariat
     */
    public String getPassword() {
        return password;
    }

}
