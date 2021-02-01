package util.email;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata Concrete Observer che alla notifica richiama il client ServiceMail {@link ServiceMail} per inviare un email di notifica al cliente.
 */
public class ConcreteObserver implements Observer {
    private final String emailOwner;
    private final LocalDate dataVisit;
    private final LocalTime timeStartVisit;
    private final LocalTime timeEndVisit;

    /**
     * Metodo costruttore di un oggetto ConcreteObserver tramite l'oggetto Builder creato
     *
     * @param builder oggetto Builder {@link ConcreteObserver.Builder}
     */
    public ConcreteObserver(Builder builder) {
        emailOwner = builder.emailOwner;
        dataVisit = builder.dataVisit;
        timeStartVisit = builder.timeStartVisit;
        timeEndVisit = builder.timeEndVisit;
    }

    /**
     * Classe utilizzata per creare un oggetto 'ConcreteObserver':{@link ConcreteObserver}
     */
    public static class Builder {
        private String emailOwner;
        private LocalDate dataVisit;
        private LocalTime timeStartVisit;
        private LocalTime timeEndVisit;

        /**
         * Metodo costruttore default del Builder
         */
        public Builder() {
        }

        /**
         * Metodo che setta l'email dell'owner (chi riceve l'email).
         *
         * @param emailOwner l'email dell'owner (chi riceve l'email) da inserire in ConcreteObserver.
         * @return Builder per la costruzione dell'oggetto ConcreteObserver.
         */
        public Builder setEmailOwner(String emailOwner) {
            this.emailOwner = emailOwner;
            return this;
        }

        /**
         * Metodo che setta la data della visita da inserire nel testo dell'email.
         *
         * @param dataVisit la data della visita da inserire in ConcreteObserver.
         * @return Builder per la costruzione dell'oggetto ConcreteObserver.
         */
        public Builder setDataVisit(LocalDate dataVisit) {
            this.dataVisit = dataVisit;
            return this;
        }

        /**
         * Metodo che setta l'orario di inizio della visita da inserire nel testo dell'email.
         *
         * @param timeStartVisit orario di inizio della visita da inserire in ConcreteObserver.
         * @return Builder per la costruzione dell'oggetto ConcreteObserver.
         */
        public Builder setTimeStartVisit(LocalTime timeStartVisit) {
            this.timeStartVisit = timeStartVisit;
            return this;
        }

        /**
         * Metodo che setta l'orario di fine (prevista) della visita da inserire nel testo dell'email.
         *
         * @param timeEndVisit orario di fine (prevista) della visita da inserire nell'oggetto ConcreteObserver.
         * @return Builder per la costruzione dell'oggetto ConcreteObserver.
         */
        public Builder setTimeEndVisit(LocalTime timeEndVisit) {
            this.timeEndVisit = timeEndVisit;
            return this;
        }

        /**
         * Metodo che effettuata la costruzione di un nuovo ConcreteObserver e lo restituisce
         *
         * @return ConcreteObserver creato con i metodi del Builder {@link ConcreteObserver.Builder}
         */
        public ConcreteObserver build() {
            return new ConcreteObserver(this);
        }
    }

    /**
     * Metodo che restituisce l'email dell'owner (chi riceve l'email)
     *
     * @return restituisce l'email dell'owner
     */
    public String getEmailOwner() {
        return emailOwner;
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
     * Questo metodo, quando l'oggetto osservato è cambiato, richiama il ServiceMail responsabile della creazione della catena di handler che gestiscono l'invio dell'email
     */
    @Override
    public void update() {
        ServiceMail.sendInfoMail(this.getEmailOwner(), this.getDataVisit(), this.getTimeStartVisit(), this.getTimeEndVisit()); //passare i parametri qui //immettere l'email a cui inviare il messaggio
    }
}
