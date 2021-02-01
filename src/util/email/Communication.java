package util.email;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per rappresentare un oggetto 'Communication':{@link Communication}
 * il quale contiene i parametri necessari per una communicazione per mezzo email, ed altri parametri da inserire nel testo della email da mandare.
 */
public class Communication {
    private final String myAccountEmail; //sender
    private final String myPassword;
    private final String receiver;       //receiver

    //parametri testo email
    private final LocalDate dataVisit;
    private final LocalTime timeStartVisit;
    private final LocalTime timeEndVisit;

    /**
     * Metodo costruttore di un oggetto Communication tramite l'oggetto Builder creato
     *
     * @param builder oggetto Builder {@link Communication.Builder}
     */
    public Communication(Builder builder) {
        myAccountEmail = builder.myAccountEmail;
        myPassword = builder.myPassword;
        receiver = builder.receiver;
        dataVisit = builder.dataVisit;
        timeStartVisit = builder.timeStartVisit;
        timeEndVisit = builder.timeEndVisit;
    }

    /**
     * Metodo che restituisce l'email del sender (chi manda l'email)
     *
     * @return restituisce l'email del sender
     */
    public String getMyAccountEmail() {
        return myAccountEmail;
    }

    /**
     * Metodo che restituisce la password del sender (chi manda l'email)
     *
     * @return restituisce la password del sender
     */
    public String getMyPassword() {
        return myPassword;
    }

    /**
     * Metodo che restituisce l'email del receiver (chi riceve l'email)
     *
     * @return restituisce l'email del receiver
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Metodo che restituisce la data della visita da inserire nel testo dell'email
     *
     * @return la data della visita da inserire nel testo dell'email
     */
    public LocalDate getDataVisit() {
        return dataVisit;
    }

    /**
     * Metodo che restituisce l'orario di inizio della visita da inserire nel testo dell'email
     *
     * @return l'orario di inizio della visita da inserire nel testo dell'email
     */
    public LocalTime getTimeStartVisit() {
        return timeStartVisit;
    }

    /**
     * Metodo che restituisce l'orario di fine (prevista) della visita da inserire nel testo dell'email
     *
     * @return l'orario di fine (prevista) della visita da inserire nel testo dell'email
     */
    public LocalTime getTimeEndVisit() {
        return timeEndVisit;
    }

    /**
     * Classe utilizzata per creare un oggetto 'Communication':{@link Communication}
     */
    public static class Builder {
        private String myAccountEmail; //sender
        private String myPassword;
        private String receiver;

        //parametri testo email
        private LocalDate dataVisit;
        private LocalTime timeStartVisit;
        private LocalTime timeEndVisit;

        /**
         * Metodo costruttore default del Builder
         */
        public Builder() {
        }

        /**
         * Metodo che setta l'email del sender (chi manda l'email).
         *
         * @param myAccountEmail l'email del sender da inserire nell'oggetto Communication.
         * @return Builder per la costruzione dell'oggetto Communication.
         */
        public Builder setMyAccountEmail(String myAccountEmail) {
            this.myAccountEmail = myAccountEmail;
            return this;
        }

        /**
         * Metodo che setta la password del sender (chi manda l'email).
         *
         * @param myPassword la password del sender da inserire nell'oggetto Communication.
         * @return Builder per la costruzione dell'oggetto Communication.
         */
        public Builder setMyPassword(String myPassword) {
            this.myPassword = myPassword;
            return this;
        }

        /**
         * Metodo che setta l'email del receiver (chi riceve l'email).
         *
         * @param receiver l'email del receiver da inserire nell'oggetto Communication.
         * @return Builder per la costruzione dell'oggetto Communication.
         */
        public Builder setReceiver(String receiver) {
            this.receiver = receiver;
            return this;
        }

        /**
         * Metodo che setta la data della visita da inserire nel testo dell'email.
         *
         * @param dataVisit la data della visita da inserire nell'oggetto Communication.
         * @return Builder per la costruzione dell'oggetto Communication.
         */
        public Builder setDataVisit(LocalDate dataVisit) {
            this.dataVisit = dataVisit;
            return this;
        }

        /**
         * Metodo che setta l'orario di inizio della visita da inserire nel testo dell'email.
         *
         * @param timeStartVisit orario di inizio della visita da inserire nell'oggetto Communication.
         * @return Builder per la costruzione dell'oggetto Communication.
         */
        public Builder setTimeStartVisit(LocalTime timeStartVisit) {
            this.timeStartVisit = timeStartVisit;
            return this;
        }

        /**
         * Metodo che setta l'orario di fine (prevista) della visita da inserire nel testo dell'email.
         *
         * @param timeEndVisit orario di fine (prevista) della visita da inserire nell'oggetto Communication.
         * @return Builder per la costruzione dell'oggetto Communication.
         */
        public Builder setTimeEndVisit(LocalTime timeEndVisit) {
            this.timeEndVisit = timeEndVisit;
            return this;
        }

        /**
         * Metodo che effettuata la costruzione di un nuova Communication e la restituisce
         *
         * @return Communication creata con i metodi del Builder {@link Communication.Builder}
         */
        public Communication build() {
            return new Communication(this);
        }
    }

}
