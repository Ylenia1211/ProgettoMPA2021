package model;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe astratta utilizzata per rappresentare un oggetto 'Person':{@link Person} estende la classe astratta 'Masterdata':{@link MasterData}
 */
public abstract class Person extends MasterData {

    /**
     * Classe astratta generica utilizzata per creare un oggetto con i campi di 'Person':{@link Person} estende il builder della classe astratta Masterdata:{@link MasterData.Builder}
     */
    public static abstract class Builder<T extends Builder<T>>
            extends MasterData.Builder<Builder<T>> {

        protected String address;
        protected String city;
        protected String telephone;
        protected String email;
        protected String fiscalCode;

        /**
         * Metodo che permette l'inserimento del campo 'address' all'interno di un oggetto generico.
         *
         * @param address indirizzo da aggiungere all'oggetto generico.
         * @return T Builder generico
         */
        public T addAddress(String address) {
            this.address = address;
            return getThis();
        }

        /**
         * Metodo che permette l'inserimento del campo 'city' all'interno di un oggetto generico.
         *
         * @param city città da aggiungere all'oggetto generico.
         * @return T Builder generico
         */
        public T addCity(String city) {
            this.city = city;
            return getThis();
        }

        /**
         * Metodo che permette l'inserimento del campo 'telephone' all'interno di un oggetto generico.
         *
         * @param telephone telefono da aggiungere all'oggetto generico.
         * @return T Builder generico
         */
        public T addTelephone(String telephone) {
            this.telephone = telephone;
            return getThis();
        }

        /**
         * Metodo che permette l'inserimento del campo 'email' all'interno di un oggetto generico.
         *
         * @param email email da aggiungere all'oggetto generico.
         * @return T Builder generico
         */
        public T addEmail(String email) {
            this.email = email;
            return getThis();
        }

        /**
         * Metodo che permette l'inserimento del campo 'code' all'interno di un oggetto generico.
         *
         * @param code codice fiscale da aggiungere all'oggetto generico.
         * @return T Builder generico
         */
        public T addFiscalCode(String code) {
            this.fiscalCode = code;
            return getThis();
        }

        /**
         * Metodo astratto che restituisce il Builder generico
         *
         * @return T Builder generico
         */
        @Override
        public abstract T getThis();
    }

    final private String address;
    final private String city;
    final private String telephone;
    final private String email;
    final private String fiscalCode;

    /**
     * Metodo costruttore di un oggetto Person tramite l'oggetto Builder generico creato, richiama i metodi costruttori delle classi Parent: {@link MasterData.Builder}.
     *
     * @param builder oggetto Builder generico {@link Person.Builder}
     */
    protected <T extends Builder<T>> Person(Builder builder) {
        super(builder);
        this.address = builder.address;
        this.city = builder.city;
        this.telephone = builder.telephone;
        this.email = builder.email;
        this.fiscalCode = builder.fiscalCode;
    }

    /**
     * Metodo che restituisce l'indirizzo' di una Persona
     *
     * @return campo address di Person
     */
    public String getAddress() {
        return address;
    }

    /**
     * Metodo che restituisce la città di una Persona
     *
     * @return campo city di Person
     */
    public String getCity() {
        return city;
    }

    /**
     * Metodo che restituisce il telefono di una Persona
     *
     * @return campo telephone di Person
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Metodo che restituisce l'email di una Persona
     *
     * @return campo email di Person
     */
    public String getEmail() {
        return email;
    }

    /**
     * Metodo che restituisce il codice fiscale di una Persona
     *
     * @return campo fiscalCode di Person
     */
    public String getFiscalCode() {
        return fiscalCode;
    }

    /**
     * Metodo per la stampa l'oggetto Person
     */
    @Override
    public String toString() {
        return "Person{" +
                "address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", fiscalCode='" + fiscalCode + '\'' +
                '}';
    }
}

