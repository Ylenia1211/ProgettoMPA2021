package model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
public class Appointment {
    private final String id;
    private final LocalDate localDate;
    private final LocalTime localTimeStart;
    private final LocalTime localTimeEnd;
    private final String id_doctor;
    private final String specialitation;
    private final String id_owner;
    private final String id_pet;

    public Appointment(Builder builder) {
         id = builder.id;
         localDate = builder.localDate;
         localTimeStart = builder.localTimeStart;
         localTimeEnd = builder.localTimeEnd;
         id_doctor = builder.id_doctor;
         specialitation = builder.specialitation;
         id_owner = builder.id_owner;
         id_pet  = builder.specialitation;
    }

    public String getId() {
        return id;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public LocalTime getLocalTimeStart() {
        return localTimeStart;
    }

    public String getId_doctor() {
        return id_doctor;
    }

    public String getSpecialitation() {
        return specialitation;
    }

    public String getId_owner() {
        return id_owner;
    }

    public String getId_pet() {
        return id_pet;
    }

    public LocalTime getLocalTimeEnd() {
        return localTimeEnd;
    }

    public static class Builder{
        private String id;
        private LocalDate localDate;
        private LocalTime localTimeStart;
        private LocalTime localTimeEnd;
        private String id_doctor;
        private String specialitation;
        private String id_owner;
        private String id_pet;

        public Builder(){
            this.id = UUID.randomUUID().toString();
        }

        public Builder setLocalDate(LocalDate localDate) {
            this.localDate = localDate;
            return this;
        }

        public Builder setLocalTimeStart(LocalTime localTimeStart) {
            this.localTimeStart = localTimeStart;
            return this;
        }

        public Builder setLocalTimeEnd(LocalTime localTimeEnd) {
            this.localTimeEnd = localTimeEnd;
            return this;
        }

        public Builder setId_doctor(String id_doctor) {
            this.id_doctor = id_doctor;
            return this;
        }

        public Builder setSpecialitation(String specialitation) {
            this.specialitation = specialitation;
            return this;
        }

        public Builder setId_owner(String id_owner) {
            this.id_owner = id_owner;
            return this;
        }

        public Builder setId_pet(String id_pet) {
            this.id_pet = id_pet;
            return this;
        }

        public  Appointment build(){
            return new Appointment(this);
        }
    }

}
