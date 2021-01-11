package dao;

import datasource.ConnectionDBH2;
import model.Appointment;
import model.Gender;
import model.Pet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
    public void add(Appointment item) {

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
    public List<Pet> searchPetsByOwner(String id) {
        List<Pet> listPets = new ArrayList<>();
        String sqlSearchById = " SELECT * FROM masterdata" +
                "        INNER JOIN pet ON pet.id = masterdata.id" +
                "        WHERE pet.OWNER = ?";
        try {
            PreparedStatement statement = this.connection_db.dbConnection().prepareStatement(sqlSearchById);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                listPets.add(new Pet.Builder<>(rs.getString("typepet"),
                        rs.getString("owner"),
                        rs.getString("particularsign"))
                        .addName(rs.getString("name"))
                        .addSurname(rs.getString("surname"))
                        .addSex(Gender.valueOf(rs.getString("sex")))
                        .addDateBirth( LocalDate.parse(rs.getString("datebirth")))
                        .build()
                );
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
        HashMap<String,Map<String, String>> linkedMap = new HashMap<>();
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
                specializationSearched = rs.getString("specialitation");
                //System.out.println(specializationSearched);
            }
            //System.out.println("last: " + specializationSearched);
            return specializationSearched;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
