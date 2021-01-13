package model;

import util.ServiceMail;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class ConcreteObserver implements Observer {
    //aggiungere costruttore di concrete observer con campi:
    // l'indirizzo email del owner associato
    // data, ora inizio, ora fine (prevista) della visita
    private final String emailOwner;
    private final LocalDate dataVisit;
    private final LocalTime timeStartVisit;
    private final LocalTime timeEndVisit;

    public ConcreteObserver(Builder builder) {
        emailOwner = builder.emailOwner;
        dataVisit = builder.dataVisit;
        timeStartVisit = builder.timeStartVisit;
        timeEndVisit = builder.timeEndVisit;
    }

    public static class Builder{
        private  String emailOwner;
        private  LocalDate dataVisit;
        private  LocalTime timeStartVisit;
        private  LocalTime timeEndVisit;
        public Builder(){
        }

        public Builder setEmailOwner(String emailOwner) {
            this.emailOwner = emailOwner;
            return this;
        }

        public  Builder setDataVisit(LocalDate dataVisit) {
            this.dataVisit = dataVisit;
            return this;
        }

        public  Builder setTimeStartVisit(LocalTime timeStartVisit) {
            this.timeStartVisit = timeStartVisit;
            return this;
        }

        public  Builder setTimeEndVisit(LocalTime timeEndVisit) {
            this.timeEndVisit = timeEndVisit;
            return this;
        }
        public  ConcreteObserver build(){
            return new ConcreteObserver(this);
        }
    }


    public String getEmailOwner() {
        return emailOwner;
    }

    public LocalDate getDataVisit() {
        return dataVisit;
    }

    public LocalTime getTimeStartVisit() {
        return timeStartVisit;
    }

    public LocalTime getTimeEndVisit() {
        return timeEndVisit;
    }

    @Override
    public void update() {
        ServiceMail.sendMail(this.getEmailOwner(), this.getDataVisit(), this.getTimeStartVisit(), this.getTimeEndVisit()); //passare i parametri qui //immettere l'email a cui inviare il messaggio
    }
}
