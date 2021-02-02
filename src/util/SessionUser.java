package util;

import dao.ConcreteDoctorDAO;
import dao.ConcreteSecretariatDAO;
import datasource.ConnectionDBH2;
import model.Admin;
import model.Doctor;
import model.Secretariat;
import model.User;

import javax.swing.*;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per la creazione di una Sessione per un utente.
 */
public class SessionUser {
    private static final SessionUser instance = new SessionUser();
    private static User userLogged;
    private static Doctor doctor;
    private static Secretariat secretariat;
    private static Admin admin;

    /**
     * Metodo costruttore privato
     */
    private SessionUser() {
    }

    /**
     * Metodo di login della Sessione che in base al tipo di utente e al Ruolo, controlla che i campi per l'autenticazione siano coerenti con il Db,
     * e inizializza l'utente Autenticato.
     *
     * @param user oggetto di tipo {@link User}
     */
    public static void login(User user) {
        if (userLogged != null) {
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
                    admin = new Admin();
                    admin.setUsername(user.getUsername());
                    admin.setPassword(user.getPassword());
                }
                default -> throw new IllegalStateException("Unexpected value: " + user.getRole());
            }
        }
    }

    /**
     * Metodo di logout della Sessione che distrugge l'oggetto Utente indipendentemente dal ruolo.
     */
    public static void logout() {
        if (userLogged != null) {
            doctor = null;
            secretariat = null;
            admin = null;
            userLogged = null;
        } else {
            JOptionPane.showMessageDialog(null, "Nessun utente connesso! impossibile effettuare il logout");
        }
    }

    /**
     * Metodo di restituzione di un oggetto {@link User} che rappresenta l'utente loggato in fase di Autenticazione.
     *
     * @return oggetto {@link User}
     */
    public static User getUserLogged() {
        return userLogged;
    }

    /**
     * Metodo di restituzione di un oggetto {@link Secretariat} che rappresenta l'utente Secretariat loggato.
     *
     * @return oggetto {@link Secretariat}
     */
    public static Secretariat getSecretariat() {
        return secretariat;
    }

    /**
     * Metodo di restituzione di un oggetto {@link Admin} che rappresenta l'utente Admin loggato.
     *
     * @return oggetto {@link Admin}
     */
    public static Admin getAdmin() {
        return admin;
    }

    /**
     * Metodo di restituzione di un oggetto {@link Doctor} che rappresenta l'utente Doctor loggato.
     *
     * @return oggetto {@link Doctor}
     */
    public static Doctor getDoctor() {
        return doctor;
    }

    /**
     * Metodo che aggiorna l'utente loggato nella sessione nel momento in cui ci sia un aggiornamento del profilo personale {@link Doctor} che rappresenta l'utente Doctor loggato.
     *
     * @param doctor_ rappresenta l'utente Doctor loggato {@link Doctor}.
     */
    public static void updateProfile(Doctor doctor_) {
        doctor = doctor_;
    }

    /**
     * Metodo che aggiorna l'utente loggato nella sessione nel momento in cui ci sia un aggiornamento del profilo personale {@link Secretariat} che rappresenta l'utente Secretariat loggato.
     *
     * @param secretariat_ rappresenta l'utente Secretariat loggato {@link Secretariat}.
     */
    public static void updateProfile(Secretariat secretariat_) {
        secretariat = secretariat_;
    }
}
