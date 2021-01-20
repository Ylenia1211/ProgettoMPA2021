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
    private PreparedStatement ps;
    public ConcreteReportDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

    @Override
    public void add(Report report) {
        try {
            ps = connection_db.getConnectData().prepareStatement("insert into REPORT(ID, ID_BOOKING, ID_OWNER, ID_PET,DIAGNOSIS, TREATMENTS,PATHFILE) values(?,?,?,?,?,?,?)");
            ps.setString(1, report.getId());
            ps.setString(2, report.getId_booking());
            ps.setString(3, report.getId_owner());
            ps.setString(4, report.getId_pet());
            ps.setString(5, report.getDiagnosis());
            ps.setString(6, report.getTreatments());
            ps.setString(7, report.getPathFile());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Report aggiunto correttamente!");
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
    public void update(String id, Report report) {
        try {
            ps = connection_db.getConnectData().prepareStatement("""
                    update REPORT
                    set DIAGNOSIS = ?, TREATMENTS = ?, PATHFILE = ?
                    where ID_BOOKING = ?""");
            ps.setString(1, report.getDiagnosis());
            ps.setString(2, report.getTreatments());
            ps.setString(3, report.getPathFile());
            ps.setString(4, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Report modificato correttamente!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        try {
            ps = connection_db.getConnectData().prepareStatement("delete from REPORT where ID_BOOKING=?");
            ps.setString(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Report eliminato correttamente!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    @Override
    public String searchIdBookingByAppointment(Appointment appointment) {
        PreparedStatement ps = null;
        try{
            PreparedStatement statement = connection_db.getConnectData().prepareStatement("SELECT * FROM BOOKING" +
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

    @Override
    public Report searchByIdBooking(String idBooking) {
        PreparedStatement ps;
        Report report = null;
        try {
            ps = connection_db.getConnectData().prepareStatement("SELECT * FROM  REPORT  WHERE REPORT.ID_BOOKING = ?");
            ps.setString(1, idBooking);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                //costruzione report
              report = new Report.Builder()
                        .setId_booking(rs.getString("id_booking"))
                        .setId_owner(rs.getString("id_owner"))
                        .setId_pet(rs.getString("id_pet"))
                        .setDiagnosis(rs.getString("diagnosis"))
                        .setTreatments(rs.getString("treatments"))
                        .setPathFile(rs.getString("pathfile")).build();
              System.out.println(report.toString());
              JOptionPane.showMessageDialog(null, "dati report presi correttamente!");
             return report;
            }else{
                JOptionPane.showMessageDialog(null, "Ricerca Vuota");
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
        return report;
    }
}
