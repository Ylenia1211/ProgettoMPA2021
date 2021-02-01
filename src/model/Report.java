package model;

import java.util.UUID;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per rappresentare un oggetto 'Report':{@link Report}
 */
public class Report {
    private final String id;
    private final String id_booking;
    private final String id_owner;
    private final String id_pet;
    private final String diagnosis;
    private final String treatments;
    private final String pathFile;

    /**
     * Metodo costruttore di un oggetto Report tramite l'oggetto Builder creato
     *
     * @param builder oggetto Builder {@link Report.Builder}
     */
    public Report(Builder builder) {
        id = builder.id;
        id_owner = builder.id_owner;
        id_pet = builder.id_pet;
        id_booking = builder.id_booking;
        diagnosis = builder.diagnosis;
        treatments = builder.treatments;
        pathFile = builder.pathFile;
    }

    /**
     * Metodo che restituisce l'id di un Report
     *
     * @return id di un Report
     */
    public String getId() {
        return id;
    }

    /**
     * Metodo che restituisce id di un Appointment associato al Report
     *
     * @return id di un Appointment associato al Report
     */
    public String getId_booking() {
        return id_booking;
    }

    /**
     * Metodo che restituisce id di un Owner associato al Report
     *
     * @return id di un Owner associato al Report
     */
    public String getId_owner() {
        return id_owner;
    }

    /**
     * Metodo che restituisce id di un Pet associato al Report
     *
     * @return id di un Pet associato al Report
     */
    public String getId_pet() {
        return id_pet;
    }

    /**
     * Metodo che restituisce il testo di una diagnosi associata Report
     *
     * @return diagnosi associata Report
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * Metodo che restituisce il testo di una trattamento associato Report
     *
     * @return trattamento associato Report
     */

    public String getTreatments() {
        return treatments;
    }

    /**
     * Metodo che restituisce il percorso del file allegato al Report
     *
     * @return percorso del file allegato al Report
     */
    public String getPathFile() {
        return pathFile;
    }

    /**
     * Metodo per la stampa l'oggetto Report
     */
    @Override
    public String toString() {
        return "Report{" +
                "id='" + id + '\'' +
                ", id_booking='" + id_booking + '\'' +
                ", id_owner='" + id_owner + '\'' +
                ", id_pet='" + id_pet + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", treatments='" + treatments + '\'' +
                ", pathFile='" + pathFile + '\'' +
                '}';
    }

    /**
     * Classe utilizzata per creare un oggetto 'Report':{@link Report}
     */
    public static class Builder {
        private String id;
        private String id_booking;
        private String id_owner;
        private String id_pet;
        private String diagnosis;
        private String treatments;
        private String pathFile; //allegato

        /**
         * Metodo costruttore del Builder: genera l'id randomico utilizzando {@see java.util.UUID} per l'oggetto creato.
         */
        public Builder() {
            this.id = UUID.randomUUID().toString();
        }

        /**
         * Metodo che permette l'inserimento del campo 'id_booking' all'interno del Report.
         *
         * @param id_booking id dell'Appointment associato al Report.
         * @return Builder per la costruzione dell'oggetto Report.
         */
        public Builder setId_booking(String id_booking) {
            this.id_booking = id_booking;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'id_owner' all'interno del Report.
         *
         * @param id_owner id dell'Owner associato  al Report.
         * @return Builder per la costruzione dell'oggetto Report.
         */
        public Builder setId_owner(String id_owner) {
            this.id_owner = id_owner;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'id_pet' all'interno del Report.
         *
         * @param id_pet id del Pet associato  al Report.
         * @return Builder per la costruzione dell'oggetto Report.
         */
        public Builder setId_pet(String id_pet) {
            this.id_pet = id_pet;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'diagnosis' all'interno del Report.
         *
         * @param diagnosis diagnosi associata al Report.
         * @return Builder per la costruzione dell'oggetto Report.
         */
        public Builder setDiagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'treatments' all'interno del Report.
         *
         * @param treatments trattamento associato al Report.
         * @return Builder per la costruzione dell'oggetto Report.
         */
        public Builder setTreatments(String treatments) {
            this.treatments = treatments;
            return this;
        }

        /**
         * Metodo che permette l'inserimento del campo 'pathFile' all'interno del Report.
         *
         * @param pathFile percorso del File allegato associato al Report.
         * @return Builder per la costruzione dell'oggetto Report.
         */
        public Builder setPathFile(String pathFile) {
            this.pathFile = pathFile;
            return this;
        }

        /**
         * Metodo che effettuata la costruzione di un nuovo Report lo restituisce
         *
         * @return Report creato con i metodi del Builder {@link Report.Builder}
         */
        public Report build() {
            return new Report(this);
        }
    }

}
