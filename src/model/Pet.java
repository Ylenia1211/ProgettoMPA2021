package model;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per rappresentare un oggetto 'Pet':{@link Pet} estende la classe astratta 'Masterdata':{@link MasterData}
 */
public class Pet extends MasterData {

    /**
     * Classe utilizzata per creare un oggetto 'Pet':{@link Pet} estende la classe astratta 'Builder' di Masterdata:{@link model.MasterData.Builder}
     */
    public static class Builder<T extends Pet.Builder<T>> extends MasterData.Builder<Pet.Builder<T>> {
        private String id_petRace;
        private String id_owner;
        private String particularSign;

        /**
         * Metodo costruttore default del Builder, che richiama i metodi costruttori delle classi Parent: {@link MasterData.Builder}.
         */
        public Builder() {
            super();
        }

        /**
         * Metodo costruttore che richiama i metodi costruttori delle classi Parent:  {@link MasterData.Builder}.
         *
         * @param id_petRace     setta il campo id_petRace del Pet da inserire
         * @param id_owner       setta il campo id_owner del Pet da inserire
         * @param particularSign setta il campo particulaSign del Pet da inserire
         */
        public Builder(String id_petRace, String id_owner, String particularSign) {
            super();
            this.id_petRace = id_petRace;
            this.id_owner = id_owner;
            this.particularSign = particularSign;
        }


        /**
         * Metodo che permette l'inserimento del campo 'specialization' all'interno di Pet.
         *
         * @param id_petRace razza da aggiungere al Pet.
         * @return Builder per la costruzione dell'oggetto Pet
         */
        public Builder setId_petRace(String id_petRace) {
            this.id_petRace = id_petRace;
            return this;
        }


        /**
         * Metodo che permette l'inserimento del campo 'id_owner' all'interno di Pet.
         *
         * @param id_owner id dell'Owner associato, da aggiungere al Pet.
         * @return Builder per la costruzione dell'oggetto Pet
         */
        public Builder setId_owner(String id_owner) {
            this.id_owner = id_owner;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'particularSign' all'interno di Pet.
         *
         * @param particularSign segni particolari da aggiungere al Pet.
         * @return Builder per la costruzione dell'oggetto Pet
         */
        public Builder setParticularSign(String particularSign) {
            this.particularSign = particularSign;
            return this;
        }

        /**
         * Metodo che restituisce un oggetto di tipo {@link Pet.Builder}
         *
         * @return Pet.Builder
         */
        @Override
        public Pet.Builder getThis() {
            return this;
        }

        /**
         * Metodo che effettuata la costruzione di un nuovo Pet e lo restituisce
         *
         * @return Pet creato con i metodi del Builder {@link Pet.Builder}
         */
        public Pet build() {
            return new Pet(this);
        }
    }


    final private String id_petRace;
    final private String id_owner;
    final private String particularSign;

    /**
     * Metodo costruttore di un oggetto Pet tramite l'oggetto Builder creato
     *
     * @param builder oggetto Builder {@link Pet.Builder}
     */
    protected Pet(Pet.Builder builder) {
        super(builder);
        this.id_owner = builder.id_owner;
        this.id_petRace = builder.id_petRace;
        this.particularSign = builder.particularSign;
    }

    /**
     * Metodo che restituisce la razza di un Pet
     *
     * @return la razza di un Pet
     */
    public String getId_petRace() {
        return id_petRace;
    }

    /**
     * Metodo che restituisce l'id dell'Owner a cui è associato un Pet
     *
     * @return id dell'Owner a cui è associato un Pet
     */
    public String getId_owner() {
        return id_owner;
    }

    /**
     * Metodo che restituisce i 'segni particolari' di un Pet
     *
     * @return i 'segni particolari' di un Pet
     */
    public String getParticularSign() {
        return particularSign;
    }
}
