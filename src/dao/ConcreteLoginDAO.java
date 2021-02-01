package dao;

import datasource.ConnectionDBH2;
import model.User;
import util.gui.Common;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe che implementa i metodi dell'interfaccia 'LoginDAO': {@link LoginDAO}  Data Access Object.
 * Classe che richiama i metodi dell'interfaccia 'Common': {@link Common}.
 * Serve a dialogare concretamente con il database.
 */

public class ConcreteLoginDAO implements LoginDAO, Common {
    private final ConnectionDBH2 connection_db;
    private PreparedStatement ps;

    /**
     * Metodo costruttore
     *
     * @param connection_db instanza della connessione al database.
     */
    public ConcreteLoginDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

    /**
     * Metodo che ricerca il tipo di controllo da effettuare sul database, in base all'utente loggato.
     *
     * @param userLogged oggetto di tipo User {@link User}
     */
    public boolean searchUser(User userLogged) {
        switch (userLogged.getRole()) {
            case "Dottore" -> {
                return this.searchUserNamePasswordDoctor(userLogged);
            }
            case "Segreteria" -> {
                return this.searchUserNamePasswordSecretariat(userLogged);
            }
            case "Amministratore" -> {
                return this.searchUserNamePasswordAdmin(userLogged);
            }
            default -> throw new IllegalStateException("Unexpected value: " + userLogged.getRole());
        }
    }

    /**
     * Metodo che ricerca in base all'utente loggato ADMIN ricerca nel database se la password e l'username sono esistenti e corretti.
     *
     * @param userLogged oggetto di tipo User {@link User}
     */
    public boolean searchUserNamePasswordAdmin(User userLogged) {
        String sqlSearch = "Select USERNAME, PASSWORD from ADMIN WHERE USERNAME = ? AND PASSWORD = ?";
        String username;
        String password;
        try {
            ps = connection_db.getConnectData().prepareStatement(sqlSearch);
            ps.setString(1, userLogged.getUsername());
            ps.setString(2, userLogged.getPassword());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                username = rs.getString("username");
                password = rs.getString("password");
                System.out.println("Trovati: Username = " + username + "\nPassword = " + password);
                return true;
            } else {
                searchEmpty();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return false;
        }

    }

    /**
     * Metodo che ricerca in base all'utente loggato SECRETARIAT ricerca nel database se la password e l'username sono esistenti e corretti.
     *
     * @param userLogged oggetto di tipo User {@link User}
     */
    public boolean searchUserNamePasswordSecretariat(User userLogged) {
        String sqlSearch = "Select USERNAME, PASSWORD from SECRETARIAT WHERE USERNAME = ? AND PASSWORD = ?";
        String username;
        String password;
        try {
            ps = connection_db.getConnectData().prepareStatement(sqlSearch);
            ps.setString(1, userLogged.getUsername());
            ps.setString(2, userLogged.getPassword());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                username = rs.getString("username");
                password = rs.getString("password");
                System.out.println("Trovati: Username = " + username + "\nPassword = " + password);
                return true;
            } else {
                searchEmpty();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo che ricerca in base all'utente loggato DOCTOR ricerca nel database se la password e l'username sono esistenti e corretti.
     *
     * @param userLogged oggetto di tipo User {@link User}
     */
    public boolean searchUserNamePasswordDoctor(User userLogged) {
        String sqlSearch = "Select USERNAME, PASSWORD from DOCTOR WHERE USERNAME = ? AND PASSWORD = ?";
        PreparedStatement ps;
        String username;
        String password;
        try {
            ps = connection_db.getConnectData().prepareStatement(sqlSearch);
            ps.setString(1, userLogged.getUsername());
            ps.setString(2, userLogged.getPassword());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                username = rs.getString("username");
                password = rs.getString("password");
                System.out.println("Trovati: Username = " + username + "\nPassword = " + password);
                return true;
            } else {
                searchEmpty();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return false;
        }
    }


}
