package util.email;

import dao.ConcreteAdminDAO;
import datasource.ConnectionDBH2;

import java.time.LocalDate;
import java.time.LocalTime;

import static util.email.EmailProcessor.*;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe  ServiceMail {@link ServiceMail}  responsabile della creazione della catena di handler in 'EmailProcessor' {@link EmailProcessor} che gestiscono l'invio dell'email.
 */
public class ServiceMail {

    /**
     * Metodo che manda le informazioni necessarie per l'invio della mail, e richiama la catena di Handler in 'EmailProcessor' {@link EmailProcessor}
     *
     * @param emailOwner     l'email del Owner a cui inviare la mail
     * @param dataVisit      la data della visita da inserire nel testo dell'email
     * @param timeStartVisit l'orario di inizio della visita da inserire nel testo dell'email
     * @param timeEndVisit   l'orario di fine (prevista) della visita da inserire nel testo dell'email
     */
    public static void sendInfoMail(String emailOwner, LocalDate dataVisit, LocalTime timeStartVisit, LocalTime timeEndVisit) {
        //ricerca account attuale della vetClinic
        ConcreteAdminDAO adminRepo = new ConcreteAdminDAO(ConnectionDBH2.getInstance());
        String myAccountEmail = adminRepo.searchEmailClinic();  //ACCOUNT della clinica può essere gmail o outlook gestito da chainResp
        String myPassword = adminRepo.searchPasswordClinic();

        Communication auth = new Communication.Builder()
                .setMyAccountEmail(myAccountEmail)
                .setMyPassword(myPassword)
                .setReceiver(emailOwner)
                .setDataVisit(dataVisit)
                .setTimeStartVisit(timeStartVisit)
                .setTimeEndVisit(timeEndVisit).build();
        process(auth, outlookHandler, gMailHandler);
    }
}
