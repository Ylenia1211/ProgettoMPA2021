package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per rappresentare un oggetto 'Appointment':{@link Appointment}
 */
public class Appointment {
    private final String id;
    private final LocalDate localDate;
    private final LocalTime localTimeStart;
    private final LocalTime localTimeEnd;
    private final String id_doctor;
    private final String specialitation;
    private final String id_owner;
    private final String id_pet;

    /**
     * Metodo costruttore di un oggetto Appointment tramite l'oggetto Builder creato
     *
     * @param builder oggetto Builder {@link Builder}
     */
    public Appointment(Builder builder) {
        id = builder.id;
        localDate = builder.localDate;
        localTimeStart = builder.localTimeStart;
        localTimeEnd = builder.localTimeEnd;
        id_doctor = builder.id_doctor;
        specialitation = builder.specialitation;
        id_owner = builder.id_owner;
        id_pet = builder.id_pet;
    }

    /**
     * Metodo per la stampa l'oggetto Appointment
     */
    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", localDate=" + localDate +
                ", localTimeStart=" + localTimeStart +
                ", localTimeEnd=" + localTimeEnd +
                ", id_doctor='" + id_doctor + '\'' +
                ", specialitation='" + specialitation + '\'' +
                ", id_owner='" + id_owner + '\'' +
                ", id_pet='" + id_pet + '\'' +
                '}';
    }

    /**
     * Metodo che restituisce l'id di un Appointment
     *
     * @return id di un appointment
     */
    public String getId() {
        return id;
    }

    /**
     * Metodo che restituisce la data di un Appointment
     *
     * @return la data di un appointment
     */
    public LocalDate getLocalDate() {
        return localDate;
    }

    /**
     * Metodo che restituisce l'orario di inizio di un Appointment
     *
     * @return orario di inizio di un appointment
     */
    public LocalTime getLocalTimeStart() {
        return localTimeStart;
    }

    /**
     * Metodo che restituisce l'id del doctor associato ad un Appointment
     *
     * @return id del doctor associato ad un Appointment
     */
    public String getId_doctor() {
        return id_doctor;
    }

    /**
     * Metodo che restituisce la specializzazione del doctor associato ad un Appointment
     *
     * @return la specializzazione del doctor associato ad un Appointment
     */
    public String getSpecialitation() {
        return specialitation;
    }

    /**
     * Metodo che restituisce l'id del Owner associato ad un Appointment
     *
     * @return l'id dell' owner associato ad un Appointment
     */
    public String getId_owner() {
        return id_owner;
    }

    /**
     * Metodo che restituisce l'id del Pet associato ad un Appointment
     *
     * @return l'id dell' pet associato ad un Appointment
     */
    public String getId_pet() {
        return id_pet;
    }

    /**
     * Metodo che restituisce l'orario di fine (prevista) di un Appointment
     *
     * @return orario di fine (prevista) di un Appointment
     */
    public LocalTime getLocalTimeEnd() {
        return localTimeEnd;
    }

    /**
     * Classe utilizzata per creare un oggetto 'Appointment':{@link Appointment}
     */
    public static class Builder {
        private final String id;
        private LocalDate localDate;
        private LocalTime localTimeStart;
        private LocalTime localTimeEnd;
        private String id_doctor;
        private String specialitation;
        private String id_owner;
        private String id_pet;

        /**
         * Metodo costruttore del Builder: genera l'id randomico utilizzando {@see java.util.UUID} per l'oggetto creato.
         */
        public Builder() {
            this.id = UUID.randomUUID().toString();
        }

        /**
         * Metodo che permette l'inserimento del campo 'localdate' all'interno dell'Appointment.
         *
         * @param localDate data della visita da aggiungere all'Appointment.
         * @return Builder per la costruzione dell'oggetto Appointment
         */
        public Builder setLocalDate(LocalDate localDate) {
            this.localDate = localDate;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'localTimeStart' all'interno dell'Appointment.
         *
         * @param localTimeStart orario di inizio della visita da aggiungere all'Appointment.
         * @return Builder per la costruzione dell'oggetto Appointment
         */
        public Builder setLocalTimeStart(LocalTime localTimeStart) {
            this.localTimeStart = localTimeStart;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'localTimeEnd' all'interno dell'Appointment.
         *
         * @param localTimeEnd orario di fine della visita (prevista) da aggiungere all'Appointment.
         * @return Builder per la costruzione dell'oggetto Appointment
         */
        public Builder setLocalTimeEnd(LocalTime localTimeEnd) {
            this.localTimeEnd = localTimeEnd;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'id_doctor' all'interno dell'Appointment.
         *
         * @param id_doctor id del doctor da aggiungere all'Appointment.
         * @return Builder per la costruzione dell'oggetto Appointment
         */
        public Builder setId_doctor(String id_doctor) {
            this.id_doctor = id_doctor;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'specialization' all'interno dell'Appointment.
         *
         * @param specialitation specializzazione del dottore associato alla visita da aggiungere all'Appointment.
         * @return Builder per la costruzione dell'oggetto Appointment
         */
        public Builder setSpecialitation(String specialitation) {
            this.specialitation = specialitation;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'id_owner' all'interno dell'Appointment.
         *
         * @param id_owner id dell'owner da aggiungere all'Appointment.
         * @return Builder per la costruzione dell'oggetto Appointment
         */
        public Builder setId_owner(String id_owner) {
            this.id_owner = id_owner;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'id_pet' all'interno dell'Appointment.
         *
         * @param id_pet id del Pet da aggiungere all'Appointment.
         * @return Builder per la costruzione dell'oggetto Appointment
         */
        public Builder setId_pet(String id_pet) {
            this.id_pet = id_pet;
            return this;
        }

        /**
         * Metodo che effettuata la costruzione di un nuovo Appointment lo restituisce
         *
         * @return Appointment creato con i metodi del Builder {@link Builder}
         */
        public Appointment build() {
            return new Appointment(this);
        }
    }

}
