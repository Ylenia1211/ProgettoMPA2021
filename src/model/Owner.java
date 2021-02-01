package model;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per rappresentare un oggetto 'Owner':{@link Owner} estende la classe astratta 'Person':{@link Person}
 */
public class Owner extends Person {
    /**
     * Classe utilizzata per creare un oggetto 'Owner':{@link Owner} estende la classe astratta 'Builder' di Person:{@link model.Person.Builder}
     */
    public static class Builder<T extends Builder<T>>  extends Person.Builder<Builder<T>>{
        private int tot_animal;

        /**
         * Metodo costruttore default del Builder, che richiama i metodi costruttori delle classi Parent: {@link Person.Builder}, {@link MasterData.Builder}.
         */
        public Builder(){
            super();
        }
        /**
         * Metodo che permette l'inserimento del campo 'tot_animal' all'interno di Owner.
         *
         * @param tot_animal numero totale di animali che possiede un Owner.
         * @return Builder per la costruzione dell'oggetto Owner
         */
        public Builder setTotAnimal(int tot_animal) {
            this.tot_animal = tot_animal;
            return this;
        }

        /**
         * Metodo che restituisce un oggetto di tipo {@link Owner.Builder}
         *
         * @return Owner.Builder
         */
        @Override
        public Builder getThis(){
            return  this;
        }

        /**
         * Metodo che effettuata la costruzione di un nuovo Owner e lo restituisce
         *
         * @return Owner creato con i metodi del Builder {@link Owner.Builder}
         */
        public Owner build(){
            return new Owner(this);
        }
    }


    final private int tot_animal;

    /**
     * Metodo costruttore di un oggetto Owner tramite l'oggetto Builder creato
     *
     * @param builder oggetto Builder {@link Owner.Builder}
     */
    protected Owner(Builder builder){
        super (builder);
        this.tot_animal = builder.tot_animal;
    }

    public int getTot_animal() {
        return tot_animal;
    }

    /*
    public Owner(String name, String surname, Gender sex, LocalDate datebirth, String address, String city, String telephone, String email) {
        super(name, surname, sex, datebirth, address, city, telephone, email);
        tot_visit = 0;
    }
     */
}
