package dao;

import datasource.ConnectionDBH2;
import model.Appointment;
import model.Report;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConcreteReportDAO implements ReportDAO {
    private final ConnectionDBH2 connection_db;
    public  ConcreteReportDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }
    @Override
    public void add(Report item) {

    }

    @Override
    public ResultSet findAll() {
        return null;
    }

    @Override
    public void update(String id, Report item) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public String searchIdBookingByAppointment(Appointment appointment) {
        PreparedStatement ps = null;
        try{
            PreparedStatement statement = connection_db.dbConnection().prepareStatement("SELECT * FROM BOOKING" +
                    "  WHERE BOOKING.ID_OWNER = ? AND BOOKING.ID_PET = ? AND BOOKING.DATE_VISIT = ? AND BOOKING.TIME_START = ? AND BOOKING.TIME_END = ? AND BOOKING.ID_DOCTOR = ? AND  BOOKING.SPECIALIZATION = ?");
            statement.setString(1, appointment.getId_owner());
            statement.setString(2, appointment.getId_pet());
            statement.setString(3, appointment.getLocalDate().toString());
            statement.setString(4, appointment.getLocalTimeStart().toString());
            statement.setString(5, appointment.getLocalTimeEnd().toString());
            statement.setString(6, appointment.getId_doctor());
            statement.setString(7, appointment.getSpecialitation());
            ResultSet rs = statement.executeQuery();
            String id_searched ="";
            if(rs.next()){
                id_searched  = rs.getString("id");
                return id_searched;
            }else{
                JOptionPane.showMessageDialog(null, "Ricerca Vuota");
                return id_searched;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return null;
        }
    }
}
