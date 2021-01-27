package util.email;

import dao.ConcreteAdminDAO;
import datasource.ConnectionDBH2;

import java.time.LocalDate;
import java.time.LocalTime;
import static util.email.EmailProcessor.*;


public class ServiceMail {

    public static void sendInfoMail(String emailOwner, LocalDate dataVisit, LocalTime timeStartVisit, LocalTime timeEndVisit) {
         //ricerca account attuale della vetClinic

        ConcreteAdminDAO adminRepo = new ConcreteAdminDAO(ConnectionDBH2.getInstance());
        String myAccountEmail =  adminRepo.searchEmailClinic();  //ACCOUNT della clinica (può essere gmail o outlook gestito da chainResp
        String myPassword = adminRepo.searchPasswordClinic();

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
