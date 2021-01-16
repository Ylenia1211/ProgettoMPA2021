package model;

import java.util.UUID;

public class Report {
    private final String id;
    private final String id_booking;
    private final String id_owner;
    private final String id_pet;
    private final String diagnosis;
    private final String treatments;
    private final String pathFile; //allegato

    public Report(Builder builder) {
        id = builder.id;
        id_owner = builder.id_owner;
        id_pet  = builder.id_pet;
        id_booking =  builder.id_booking;
        diagnosis =  builder.diagnosis;
        treatments =  builder.treatments;
        pathFile =  builder.pathFile;
    }

    public String getId() {
        return id;
    }

    public String getId_booking() {
        return id_booking;
    }

    public String getId_owner() {
        return id_owner;
    }

    public String getId_pet() {
        return id_pet;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatments() {
        return treatments;
    }

    public String getPathFile() {
        return pathFile;
    }

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

    public static class Builder {
        private String id;
        private String id_booking;
        private String id_owner;
        private String id_pet;
        private String diagnosis;
        private String treatments;
        private String pathFile; //allegato

        public Builder(){
            this.id = UUID.randomUUID().toString();
        }

        public  Builder setId_booking(String id_booking) {
            this.id_booking = id_booking;
            return this;
        }

        public  Builder setId_owner(String id_owner) {
            this.id_owner = id_owner;
            return this;
        }

        public  Builder setId_pet(String id_pet) {
            this.id_pet = id_pet;
            return this;
        }

        public  Builder setDiagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
            return this;
        }

        public  Builder setTreatments(String treatments) {
            this.treatments = treatments;
            return this;
        }

        public  Builder setPathFile(String pathFile) {
            this.pathFile = pathFile;
            return this;
        }
        public  Report build(){
            return new Report(this);
        }
    }

}
