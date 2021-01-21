package dao;

import datasource.ConnectionDBH2;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import model.Doctor;
import model.Gender;
import model.User;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;


public class ConcreteLoginDAO{
     private final ConnectionDBH2 connection_db;
     public  ConcreteLoginDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

   /* //
    * Metodo che mi ricerca in base all'utente loggato se la password e l'username sono esistenti e corretti
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
                return  this.searchUserNamePasswordAdmin(userLogged);
            }
            default -> throw new IllegalStateException("Unexpected value: " + userLogged.getRole());
        }
    }

    private boolean searchUserNamePasswordAdmin(User userLogged) {
        String sqlSearch = "Select USERNAME, PASSWORD from ADMIN WHERE USERNAME = ? AND PASSWORD = ?";
        PreparedStatement ps = null;
        String username;
        String password;
        try {
            //ps = connection_db.dbConnection().prepareStatement(sqlSearch);

            ps = connection_db.getConnectData().prepareStatement(sqlSearch);
            ps.setString(1, userLogged.getUsername());
            ps.setString(2, userLogged.getPassword());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                username  = rs.getString("username");
                password =  rs.getString("password");
                System.out.println( "Trovati: Username = " + username + "\nPassword = "+ password);
                return true;
            }else{
                //JOptionPane.showMessageDialog(null, "Ricerca Vuota");
                searchEmpty();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return false;
        }

    }

    private boolean searchUserNamePasswordSecretariat(User userLogged) {
        String sqlSearch = "Select USERNAME, PASSWORD from SECRETARIAT WHERE USERNAME = ? AND PASSWORD = ?";
        PreparedStatement ps = null;
        String username;
        String password;
        try {
            ps = connection_db.getConnectData().prepareStatement(sqlSearch);
            ps.setString(1, userLogged.getUsername());
            ps.setString(2, userLogged.getPassword());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                username  = rs.getString("username");
                password =  rs.getString("password");
                System.out.println( "Trovati: Username = " + username + "\nPassword = "+ password);
                return true;
            }else{
                //JOptionPane.showMessageDialog(null, "Ricerca Vuota");
                searchEmpty();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return false;
        }
    }

    public boolean searchUserNamePasswordDoctor(User userLogged){
        String sqlSearch = "Select USERNAME, PASSWORD from DOCTOR WHERE USERNAME = ? AND PASSWORD = ?";
        PreparedStatement ps = null;
        String username;
        String password;
        try {
            ps = connection_db.getConnectData().prepareStatement(sqlSearch);
            ps.setString(1, userLogged.getUsername());
            ps.setString(2, userLogged.getPassword());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                username  = rs.getString("username");
                password =  rs.getString("password");
                System.out.println( "Trovati: Username = " + username + "\nPassword = "+ password);
                return true;
            }else{
                //JOptionPane.showMessageDialog(null, "Ricerca Vuota");
                searchEmpty();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return false;
        }
    }

    public static void searchEmpty(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Ricerca Vuota");
        alert.setContentText("Nessun Utente trovato con queste credenziali! Riprova.");
        alert.showAndWait();
    }
/*
    public Object searchUserNamePasswordDoctor(User userLogged){
        PreparedStatement ps = null;
       String sqlSearch = "SELECT * FROM masterdata" +
                "    INNER JOIN person" +
                "    ON person.id = masterdata.id" +
                "    INNER JOIN DOCTOR  " +
                "    ON  person.id = DOCTOR.id WHERE DOCTOR.USERNAME = ? AND DOCTOR.PASSWORD = ? ";
        try {
            ps = connection_db.getConnectData().prepareStatement(sqlSearch);
            ps.setString(1, userLogged.getUsername());
            ps.setString(2, userLogged.getPassword());
            ResultSet r = ps.executeQuery();
            if(r.next()){
                return new Doctor.Builder<>()
                        .addName(r.getString("name"))
                        .addSurname(r.getString("surname"))
                        .addSex(Gender.valueOf(r.getString("sex")))
                        .addDateBirth(LocalDate.parse(r.getString("datebirth")))
                        .addFiscalCode(r.getString("fiscalcode"))
                        .addAddress(r.getString("address"))
                        .addCity(r.getString("city"))
                        .addTelephone(r.getString("telephone"))
                        .addEmail(r.getString("email"))
                        .addSpecialization(r.getString("specialization"))
                        .addUsername(r.getString("username"))
                        .addPassword(r.getString("password"))
                        .build();
            }else{
                searchEmpty();
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return null;
        }
    }
*/
}
