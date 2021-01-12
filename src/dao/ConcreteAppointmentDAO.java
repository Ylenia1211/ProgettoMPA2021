package dao;

import datasource.ConnectionDBH2;
import model.Appointment;
import model.Gender;
import model.Pet;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConcreteAppointmentDAO implements AppointmentDAO {
    private final ConnectionDBH2 connection_db;

    public  ConcreteAppointmentDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

    @Override
    public void add(Appointment appointment) {
        PreparedStatement ps;
        try {
            ps = connection_db.dbConnection().prepareStatement("insert into BOOKING(ID, DATE_VISIT, TIME_START, TIME_END, ID_DOCTOR, SPECIALIZATION,ID_OWNER, ID_PET) values(?,?,?,?,?,?,?,?)");
            ps.setString(1,  appointment.getId());
            ps.setString(2, appointment.getLocalDate().toString());
            ps.setString(3,  appointment.getLocalTimeStart().toString());
            ps.setString(4, appointment.getLocalTimeEnd().toString());
            ps.setString(5, appointment.getId_doctor());
            ps.setString(6, appointment.getSpecialitation());
            ps.setString(7, appointment.getId_owner());
            ps.setString(8, appointment.getId_pet());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Visita aggiunta correttamente!");
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
    public void update(String id, Appointment item) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Map<String, String> searchAllClientByFiscalCod() {
        HashMap<String,Map<String, String>> linkedMap = new HashMap<>();
        Map<String, String> dictionary = new HashMap<>();  //<key,value>  both key and value are Strings
        PreparedStatement ps = null;
        String sqlSearch = "SELECT * FROM masterdata" +
                "                  INNER JOIN person" +
                "                             ON person.id = masterdata.id" +
                "                  INNER JOIN owner" +
                "                             ON  person.id = owner.id";

        try {
            PreparedStatement statement = this.connection_db.dbConnection().prepareStatement(sqlSearch);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                dictionary.put( rs.getString("id"), rs.getString("fiscalcode"));
            }
            //dictionary.entrySet().forEach(System.out::println);
            return dictionary;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, String> searchPetsByOwner(String id) {
        Map<String, String> listPets = new HashMap<>();
        String sqlSearchById = " SELECT * FROM masterdata" +
                "        INNER JOIN pet ON pet.id = masterdata.id" +
                "        WHERE pet.OWNER = ?";
        try {
            PreparedStatement statement = this.connection_db.dbConnection().prepareStatement(sqlSearchById);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                /*
                listPets.add(new Pet.Builder<>(rs.getString("typepet"),
                        rs.getString("owner"),
                        rs.getString("particularsign"))
                        .addName(rs.getString("name"))
                        .addSurname(rs.getString("surname"))
                        .addSex(Gender.valueOf(rs.getString("sex")))
                        .addDateBirth( LocalDate.parse(rs.getString("datebirth")))
                        .build()
                );*/
                listPets.put( rs.getString("id"), rs.getString("name"));
            }
            //listPets.stream().map(Pet::getId_owner).forEach(System.out::println);
            return listPets;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }


    //#Todo: bisogna cambiare il surname con il codice fiscale
    @Override
    public Map<String, String> searchAllDoctorByFiscalCod() {
        //HashMap<String,Map<String, String>> linkedMap = new HashMap<>();
        Map<String, String> dictionary = new HashMap<>();  //<key,value>  both key and value are Strings
        PreparedStatement ps = null;
        String sqlSearch = "SELECT * FROM masterdata" +
                "                  INNER JOIN person" +
                "                             ON person.id = masterdata.id" +
                "                  INNER JOIN DOCTOR" +
                "                             ON  person.id = DOCTOR.ID";

        try {
            PreparedStatement statement = this.connection_db.dbConnection().prepareStatement(sqlSearch);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                //dictionary.put( rs.getString("id"), rs.getString("fiscalcode"));
                dictionary.put( rs.getString("id"), rs.getString("surname"));
            }
            dictionary.entrySet().forEach(System.out::println);
            return dictionary;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String searchSpecializationByDoctor(String idDoctorSearched) {
        String specializationSearched ="";
        //System.out.println(idDoctorSearched);
        String sqlSearch = "SELECT * FROM masterdata" +
                "                  INNER JOIN person" +
                "                             ON person.id = masterdata.id" +
                "                  INNER JOIN DOCTOR" +
                "                             ON  person.id = DOCTOR.ID WHERE DOCTOR.ID = ? ";
        try {
            PreparedStatement statement = this.connection_db.dbConnection().prepareStatement(sqlSearch);
            statement.setString(1, idDoctorSearched);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                specializationSearched = rs.getString("specialization");
                //System.out.println(specializationSearched);
            }
            //System.out.println("last: " + specializationSearched);
            return specializationSearched;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Appointment> searchAppointmentsByDate(String date) {
        List<Appointment> listAppointment = new ArrayList<>();
        String sqlSearch = "SELECT * FROM booking WHERE BOOKING.DATE_VISIT = ? ";
        try {
            PreparedStatement statement = this.connection_db.dbConnection().prepareStatement(sqlSearch);
            statement.setString(1, date);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){

                listAppointment.add(new Appointment.Builder()
                        .setLocalDate(LocalDate.parse(rs.getString("date_visit")))
                        .setLocalTimeStart(LocalTime.parse(rs.getString("time_start")))
                        .setLocalTimeEnd(LocalTime.parse(rs.getString("time_end")))
                        .setId_doctor(rs.getString("id_doctor"))
                        .setSpecialitation(rs.getString("specialization"))
                        .setId_owner(rs.getString("id_owner"))
                        .setId_pet(rs.getString("id_pet"))
                        .build()
                );
            }
            //listAppointment.stream().map(Appointment::toString).forEach(System.out::println);
            return listAppointment;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer countAppointmentsByDate(String date) {
        Integer countDayVisits = 0;
        String sqlSearch = "SELECT COUNT(date_visit) as count_visit FROM BOOKING\n" +
                "        WHERE DATE_VISIT= ?";
        try {
            PreparedStatement statement = this.connection_db.dbConnection().prepareStatement(sqlSearch);
            statement.setString(1, date);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                     countDayVisits = rs.getInt("count_visit");
            }
            System.out.println(countDayVisits);
            return countDayVisits;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return countDayVisits;
        }
    }
}
