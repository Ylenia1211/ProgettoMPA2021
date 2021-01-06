package dao;

import datasource.ConnectionDBH2;
import model.Doctor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConcreteDoctorDAO implements DoctorDAO {
    private ConnectionDBH2 connection_db;

    public ConcreteDoctorDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

    @Override
    public void add(Doctor item) {

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
    public void searchAllSpecialization() {
        PreparedStatement ps = null;

        String sqlSearchSpecialization = "SELECT * FROM specialitation";
        try {
            PreparedStatement statement = this.connection_db.dbConnection().prepareStatement(sqlSearchSpecialization);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
