package util;

import controller.LoginController;
import dao.ConcreteDoctorDAO;
import dao.ConcreteSecretariatDAO;
import datasource.ConnectionDBH2;
import model.Admin;
import model.Doctor;
import model.Secretariat;
import model.User;

public class SessionUser { //singleton per la connessione dell'utente
    private static  SessionUser instance = new SessionUser();
    private static User user;
    private static Doctor doctor;
    private static Secretariat secretariat;
    private static Admin admin;
    private SessionUser() {

        user = LoginController.getInstance().getUserLogged();
        switch (user.getRole()) {
            case "Dottore" -> {
                ConcreteDoctorDAO doctorRepo = new ConcreteDoctorDAO(ConnectionDBH2.getInstance());
                doctor = doctorRepo.searchByUsernameAndPassword(user);
            }
            case "Segreteria" -> {
                ConcreteSecretariatDAO secretariatRepo = new ConcreteSecretariatDAO(ConnectionDBH2.getInstance());
                secretariat = secretariatRepo.searchByUsernameAndPassword(user);
            }
            case "Amministratore" -> {
                      admin.setUsername(user.getUsername());
                      admin.setPassword(user.getPassword());
            }
            default -> throw new IllegalStateException("Unexpected value: " + user.getRole());
        }
    }

    public static Secretariat getSecretariat() {
        return secretariat;
    }

    public static Admin getAdmin() {
        return admin;
    }

    public static Doctor getDoctor() {
        return doctor;
    }

    public static User getUser() {
        return user;
    }

    public static  SessionUser getInstance() {
        return instance;
    }


}
