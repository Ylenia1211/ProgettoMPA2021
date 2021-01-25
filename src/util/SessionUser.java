package util;

import controller.LoginController;
import dao.ConcreteDoctorDAO;
import dao.ConcreteSecretariatDAO;
import datasource.ConnectionDBH2;
import model.Admin;
import model.Doctor;
import model.Secretariat;
import model.User;

import javax.swing.*;
import java.util.Objects;

public class SessionUser { //singleton per la connessione dell'utente
    private static SessionUser instance = new SessionUser();
    private static User userLogged;
    private static Doctor doctor;
    private static Secretariat secretariat;
    private static Admin admin;


    private SessionUser() {
        // user = LoginController.getUserLogged();

    }

    public  static void login(User user) {
        if  (userLogged!= null) {
            JOptionPane.showMessageDialog(null, "Già un utente connesso! impossibile effettuare il login");

        } else {
            userLogged = user;
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
    }
    public static void logout() {
        if (userLogged!= null) {
                doctor = null;
                secretariat=null;
                admin=null;
                userLogged =null;
        }else{
            JOptionPane.showMessageDialog(null, "Nessun utente connesso! impossibile effettuare il logout");
        }
    }

    public static User getUserLogged() {
        return userLogged;
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

}
