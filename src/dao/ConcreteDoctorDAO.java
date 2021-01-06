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
            //ps = connection_db.dbConnection().prepareStatement("insert into masterdata(id, name, surname,sex, datebirth) values(?,?,?,?,?)");
            ps.setString(1, doctor.getId());
            ps.setString(2, doctor.getName());
            ps.setString(3, doctor.getSurname());
            ps.setString(4, doctor.getSex());
            //LocalDate ld = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(owner.getDatebirth()));
            ps.setString(5, doctor.getDatebirth().toString());
            ps.executeUpdate();
            System.out.println("Anagrafica Doctor aggiunta al DB!");


            ps = null;
           // ps = connection_db.dbConnection().prepareStatement("insert into person(id, address, city, telephone, email) values(?,?,?,?,?)");
            ps.setString(1, doctor.getId());
            ps.setString(2, doctor.getAddress());
            ps.setString(3, doctor.getCity());
            ps.setString(4, doctor.getTelephone());
            ps.setString(5, doctor.getEmail());
            ps.executeUpdate();
            System.out.println("Dati civici Doctor aggiunti al DB!");

            ps = null;
           // ps = connection_db.dbConnection().prepareStatement("insert into doctor(id, specialitation, username, password) values(?,?,?,?)");
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


    @Override
    public ResultSet findAll() {
        return null;
    }

    @Override
    public void update(String id, Doctor item) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public List<String> searchAllSpecialization() {
        List<String> listSpecialitation = new ArrayList<String>();
        PreparedStatement ps = null;

        //String sqlSearchSpecialization = "SELECT * FROM specialitation";
        String sqlSearchSpecialization="";
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
