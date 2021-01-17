package util;

import model.Report;

import java.time.LocalDate;
import java.time.LocalTime;
//informazioni sulla comunicazione
public class Communication {
    private final String myAccountEmail ; //sender
    private final String myPassword;
    private final String receiver;

    //parametri testo email
    private final LocalDate dataVisit;
    private final LocalTime timeStartVisit;
    private final LocalTime timeEndVisit;

    public Communication(Builder builder) {
        myAccountEmail = builder.myAccountEmail;
        myPassword = builder.myPassword;
        receiver = builder.receiver;
        dataVisit = builder.dataVisit;
        timeStartVisit = builder.timeStartVisit;
        timeEndVisit = builder.timeEndVisit;
    }

    public String getMyAccountEmail() {
        return myAccountEmail;
    }

    public String getMyPassword() {
        return myPassword;
    }

    public String getReceiver() {
        return receiver;
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

    public static class Builder {
        private String myAccountEmail ; //sender
        private String myPassword;
        private String receiver;

        //parametri testo email
        private LocalDate dataVisit;
        private LocalTime timeStartVisit;
        private LocalTime timeEndVisit;

        public Builder() {
        }

        public Builder setMyAccountEmail(String myAccountEmail) {
            this.myAccountEmail = myAccountEmail;
            return this;
        }

        public Builder setMyPassword(String myPassword) {
            this.myPassword = myPassword;
            return this;
        }

        public Builder setReceiver(String receiver) {
            this.receiver = receiver;
            return this;
        }

        public Builder setDataVisit(LocalDate dataVisit) {
            this.dataVisit = dataVisit;
            return this;
        }

        public Builder setTimeStartVisit(LocalTime timeStartVisit) {
            this.timeStartVisit = timeStartVisit;
            return this;
        }

        public Builder setTimeEndVisit(LocalTime timeEndVisit) {
            this.timeEndVisit = timeEndVisit;
            return this;
        }

        public Communication build(){
            return new Communication(this);
        }
    }

}
