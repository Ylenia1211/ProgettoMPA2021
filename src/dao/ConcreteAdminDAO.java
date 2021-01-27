package dao;

import datasource.ConnectionDBH2;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConcreteAdminDAO implements AdminDAO{
    private final ConnectionDBH2 connection_db;
    public  ConcreteAdminDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

    public String searchEmailClinic(){
        String email = "";
        String sqlSearch = "SELECT EMAIL FROM ADMIN" +
                " WHERE ADMIN.ID = 1";
        try {
            PreparedStatement ps = connection_db.getConnectData().prepareStatement(sqlSearch);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                email = rs.getString("email");
            }else {
                JOptionPane.showMessageDialog(null, "Impossibile recuperare l'email'!");
            }
            return email;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return email;
        }
    }


    public String searchPasswordClinic() {
        String psw = "";
        String sqlSearch = "SELECT EMAIL_PSW FROM ADMIN" +
                " WHERE ADMIN.ID = 1";
        try {
            PreparedStatement ps = connection_db.getConnectData().prepareStatement(sqlSearch);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                psw = rs.getString("email_psw");
            }else {
                JOptionPane.showMessageDialog(null, "Impossibile recuperare la password!");
            }
            return psw;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return psw;
        }
    }

    public void updatePasswordClinic(String psw){
        String sqlSearch = "UPDATE ADMIN" +
                " SET ADMIN.EMAIL_PSW = ? WHERE ADMIN.ID = 1";
        try {
            PreparedStatement ps = connection_db.getConnectData().prepareStatement(sqlSearch);
            ps.setString(1, psw);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Password email clinica Aggiornata!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    public void updateEmailClinic(String email) {
        String sqlSearch = "UPDATE ADMIN" +
                " SET ADMIN.EMAIL = ? WHERE ADMIN.ID = 1";
        try {
            PreparedStatement ps = connection_db.getConnectData().prepareStatement(sqlSearch);
            ps.setString(1, email);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Email clinica Aggiornata!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

}
