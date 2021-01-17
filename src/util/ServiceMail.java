package util;

import java.time.LocalDate;
import java.time.LocalTime;
import static util.EmailProcessor.*;


public class ServiceMail {

    public static void sendInfoMail(String emailOwner, LocalDate dataVisit, LocalTime timeStartVisit, LocalTime timeEndVisit) {
         //#todo:ricerca account attuale della vetClinic
        String myAccountEmail = "vetclinicmanagement@gmail.com";  //ACCOUNT PREDEFINITO della clinica (puo essere gmail o outlook)
        String myPassword = "VetClinicApp2021";

        Communication auth = new Communication.Builder()//builder
                .setMyAccountEmail(myAccountEmail)
                .setMyPassword(myPassword)
                .setReceiver(emailOwner)
                .setDataVisit(dataVisit)
                .setTimeStartVisit(timeStartVisit)
                .setTimeEndVisit(timeEndVisit).build();
        process(auth, outlookHandler, gMailHandler);
    }
}
