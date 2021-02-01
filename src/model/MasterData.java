package model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe astratta utilizzata per rappresentare un oggetto 'MasterData':{@link MasterData}.
 */
public abstract class MasterData {

    /**
     * Classe astratta generica utilizzata per creare un oggetto con i campi di 'Masterdata':{@link MasterData}
     */
    public static abstract class Builder<T extends Builder<T>> {
        protected String id;
        protected String name;
        protected String surname;
        protected Gender sex;
        protected LocalDate datebirth;

        /**
         * Metodo costruttore del Builder: genera l'id randomico utilizzando {@see java.util.UUID} per l'oggetto creato.
         */
        public Builder() {
            this.id = UUID.randomUUID().toString();
        }

        /**
         * Metodo che permette l'inserimento del campo 'name' all'interno di un oggetto generico.
         *
         * @param name nome da aggiungere all'oggetto generico.
         * @return T Builder generico
         */
        public T addName(String name) {
            this.name = name;
            return getThis();
        }

        /**
         * Metodo che permette l'inserimento del campo 'surname' all'interno di un oggetto generico.
         *
         * @param surname cognome da aggiungere all'oggetto generico.
         * @return T Builder generico
         */
        public T addSurname(String surname) {
            this.surname = surname;
            return getThis();
        }

        /**
         * Metodo che permette l'inserimento del campo 'sex' all'interno di un oggetto generico.
         *
         * @param sex genere {@link Gender} da aggiungere all'oggetto generico
         * @return T Builder generico
         */
        public T addSex(Gender sex) {
            this.sex = sex;
            return getThis();
        }

        /**
         * Metodo che permette l'inserimento del campo 'datebirth' all'interno di un oggetto generico.
         *
         * @param datebirth data di nascita da aggiungere all'oggetto generico.
         * @return T Builder generico
         */
        public T addDateBirth(LocalDate datebirth) {
            this.datebirth = datebirth;
            return getThis();
        }

        /**
         * Metodo astratto che restituisce il Builder generico
         *
         * @return T Builder generico
         */
        public abstract T getThis();
    }

    final private String id;
    final private String name;
    final private String surname;
    final private Gender sex;
    final private LocalDate datebirth;

    /**
     * Metodo costruttore di un oggetto MasterData tramite l'oggetto Builder generico creato
     *
     * @param builder oggetto Builder generico {@link MasterData.Builder}
     */
    protected <T extends Builder<T>> MasterData(Builder<T> builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.surname = builder.surname;
        this.sex = builder.sex;
        this.datebirth = builder.datebirth;
    }

    /**
     * Metodo che restituisce l'id attribuito a un oggetto di tipo Masterdata
     *
     * @return id di un oggetto di tipo Masterdata
     */
    public String getId() {
        return this.id;
    }

    /**
     * Metodo che restituisce il nome  attribuito a un oggetto di tipo Masterdata
     *
     * @return name di un oggetto di tipo Masterdata
     */
    public String getName() {
        return this.name;
    }

    /**
     * Metodo che restituisce il cognome  attribuito a un oggetto di tipo Masterdata
     *
     * @return surname di un oggetto di tipo Masterdata
     */
    public String getSurname() {
        return this.surname;
    }

    /**
     * Metodo che restituisce il genere  attribuito a un oggetto di tipo Masterdata
     *
     * @return genere {@link Gender} di un oggetto di tipo Masterdata
     */
    public Gender getSex() {
        return this.sex;
    }

    /**
     * Metodo che restituisce la data di nascita attribuita a un oggetto di tipo Masterdata
     *
     * @return data di nascita di un oggetto di tipo Masterdata
     */
    public LocalDate getDatebirth() {
        return this.datebirth;
    }

}
