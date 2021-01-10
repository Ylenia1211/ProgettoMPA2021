package dao;

import datasource.ConnectionDBH2;
import model.Doctor;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConcreteDoctorDAO implements DoctorDAO {
    private ConnectionDBH2 connection_db;

    public ConcreteDoctorDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

    @Override
    public void add(Doctor doctor) {
        PreparedStatement ps = null;
        try {
            ps = connection_db.dbConnection().prepareStatement("insert into masterdata(id, name, surname,sex, datebirth) values(?,?,?,?,?)");
            ps.setString(1, doctor.getId());
            ps.setString(2, doctor.getName());
            ps.setString(3, doctor.getSurname());
            ps.setString(4, doctor.getSex().toString());
            //LocalDate ld = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(owner.getDatebirth()));
            ps.setString(5, doctor.getDatebirth().toString());
            ps.executeUpdate();
            System.out.println("Anagrafica Doctor aggiunta al DB!");


            ps = null;
            ps = connection_db.dbConnection().prepareStatement("insert into person(id, address, city, telephone, email) values(?,?,?,?,?)");
            ps.setString(1, doctor.getId());
            ps.setString(2, doctor.getAddress());
            ps.setString(3, doctor.getCity());
            ps.setString(4, doctor.getTelephone());
            ps.setString(5, doctor.getEmail());
            ps.executeUpdate();
            System.out.println("Dati civici Doctor aggiunti al DB!");

            ps = null;
            ps = connection_db.dbConnection().prepareStatement("insert into doctor(id, specialitation, username, password) values(?,?,?,?)");
            ps.setString(1, doctor.getId());
            ps.setString(2, doctor.getSpecialitation());
            ps.setString(3, doctor.getUsername());
            ps.setString(4, doctor.getPassword());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Doctor aggiunto correttamente!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }

    }

    //#TODO Creare vista per mostrare i dottori e provare a modificarli
    @Override
    public ResultSet findAll() {
        try {
            PreparedStatement statement = connection_db.dbConnection().prepareStatement("SELECT * FROM DOCTOR");
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return null;
        }
    }

    @Override
    public void update(String id, Doctor item) {
        String sqlMasterData = "UPDATE DOCTOR SET SPECIALITATION = ?, USERNAME = ?, PASSWORD = ? where DOCTOR.ID = ?";

        PreparedStatement ps;
        try {
            ps = connection_db.dbConnection().prepareStatement(sqlMasterData);
            ps.setString(1, item.getSpecialitation());
            ps.setString(2, item.getUsername());
            ps.setString(3, item.getPassword());
            //#todo:manca l'id in posizione 4
            ps.executeUpdate();

            System.out.println("Aggiornati dati Anagrafica del Doctor!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        System.out.println("id da cancellare a cascata: " + id);

        PreparedStatement ps;
        try {
            ps = connection_db.dbConnection().prepareStatement("DELETE FROM DOCTOR WHERE DOCTOR.ID = "+ "'" + id + "'");
            ps.executeUpdate();
            System.out.println("Cancellati dati del Doctor!");
            JOptionPane.showMessageDialog(null, "Cancellato correttamente!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    @Override
    public List<String> searchAllSpecialization() {
        List<String> listSpecialitation = new ArrayList<String>();
        PreparedStatement ps = null;

        String sqlSearchSpecialization = "SELECT * FROM specialitation";

        try {
            PreparedStatement statement = this.connection_db.dbConnection().prepareStatement(sqlSearchSpecialization);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("name"));
                listSpecialitation.add(rs.getString("name"));
            }
            return listSpecialitation;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
