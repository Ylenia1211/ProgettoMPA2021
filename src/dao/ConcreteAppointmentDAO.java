package dao;

import datasource.ConnectionDBH2;
import model.*;

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
import java.util.stream.Collectors;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe che implementa i metodi dell'interfaccia 'AppointmentDAO': {@link AppointmentDAO}  Data Access Object.
 * Serve a dialogare concretamente con il database.
 */
public class ConcreteAppointmentDAO implements AppointmentDAO {
    private final ConnectionDBH2 connection_db;

    /**
     * Metodo costruttore
     *
     * @param connection_db instanza della connessione al database.
     */
    public ConcreteAppointmentDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

    /**
     * Metodo ereditato dall'interfaccia 'AppointmentDAO': {@link AppointmentDAO}  Data Access Object, permette di aggiungere un nuovo Appointment nel database.
     *
     * @param appointment oggetto di tipo Appointment {@link Appointment} da aggiungere nel database.
     */
    @Override
    public void add(Appointment appointment) {
        PreparedStatement ps;
        try {
            ps = connection_db.getConnectData().prepareStatement("insert into BOOKING(ID, DATE_VISIT, TIME_START, TIME_END, ID_DOCTOR, SPECIALIZATION,ID_OWNER, ID_PET) values(?,?,?,?,?,?,?,?)");
            ps.setString(1, appointment.getId());
            ps.setString(2, appointment.getLocalDate().toString());
            ps.setString(3, appointment.getLocalTimeStart().toString());
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

    /**
     * Metodo ereditato dall'interfaccia 'AppointmentDAO': {@link AppointmentDAO}  Data Access Object, permette di ricercare tutti gli Appointment presenti nel database.
     *
     * @return Lista di oggetti di tipo Appointment {@link Appointment} presenti nel database.
     */
    @Override
    public List<Appointment> findAll() {
        List<Appointment> listItems = new ArrayList<>();
        try {
            PreparedStatement statement = connection_db.getConnectData()
                    .prepareStatement(" SELECT * FROM BOOKING");
            ResultSet r = statement.executeQuery();
            while (r.next()) {
                listItems.add(new Appointment.Builder()
                        .setId_pet(r.getString("id_pet"))
                        .setId_owner(r.getString("id_owner"))
                        .setSpecialitation(r.getString("specialization"))
                        .setId_doctor(r.getString("id_doctor"))
                        .setLocalDate(LocalDate.parse(r.getString("date_visit")))
                        .setLocalTimeStart(LocalTime.parse(r.getString("time_start")))
                        .setLocalTimeEnd(LocalTime.parse(r.getString("time_end")))
                        .build()
                );
            }
            //listItems.forEach(System.out::println);
            return listItems;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return null;
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'AppointmentDAO': {@link AppointmentDAO}  Data Access Object, permette di aggiornare l'Appointment presente nel database.
     *
     * @param id          dell'Appointment {@link Appointment} da modificare nel database.
     * @param appointment Appointment che contiene i campi aggiornati da modificare nel database.
     */
    @Override
    public void update(String id, Appointment appointment) {
        String sqlUpdateAppointment = "UPDATE BOOKING SET DATE_VISIT = ?, TIME_START = ?, TIME_END = ? WHERE ID = ?";
        PreparedStatement ps;
        try {
            ps = connection_db.getConnectData().prepareStatement(sqlUpdateAppointment);
            ps.setString(1, appointment.getLocalDate().toString());
            ps.setString(2, appointment.getLocalTimeStart().toString());
            ps.setString(3, appointment.getLocalTimeEnd().toString());
            ps.setString(4, id);
            ps.executeUpdate();
            System.out.println("Aggiornati Data/ora Prenotazione!");
            JOptionPane.showMessageDialog(null, "Aggiornati Data/ora Prenotazione!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'AppointmentDAO': {@link AppointmentDAO}  Data Access Object, permette di cancellare l'Appointment presente nel database.
     *
     * @param id dell'Appointment {@link Appointment} da cancellare nel database.
     */
    @Override
    public void delete(String id) {
        PreparedStatement ps;
        try {
            ps = connection_db.getConnectData().prepareStatement("delete from BOOKING where BOOKING.ID = " + "'" + id + "'");
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Prenotazione cancellata correttamente!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }
    /**
     * Metodo ereditato dall'interfaccia 'AppointmentDAO': {@link AppointmentDAO}  Data Access Object, permette di ricercare le le coppie (id,fiscalcode) associate a un Owner presenti nel database.
     *
     * @return una mappa di coppie (id, fiscalcode) degli Owner presenti nel database.
     */
    @Override
    public Map<String, String> searchAllClientByFiscalCod() {
        Map<String, String> dictionary = new HashMap<>();  //<key,value>  both key and value are Strings
        String sqlSearch = "SELECT * FROM masterdata" +
                "                  INNER JOIN person" +
                "                             ON person.id = masterdata.id" +
                "                  INNER JOIN owner" +
                "                             ON  person.id = owner.id";

        try {
            PreparedStatement statement = this.connection_db.getConnectData().prepareStatement(sqlSearch);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                dictionary.put(rs.getString("id"), rs.getString("fiscalcode"));
            }
            //dictionary.entrySet().forEach(System.out::println);
            return dictionary;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Metodo ereditato dall'interfaccia 'AppointmentDAO': {@link AppointmentDAO}  Data Access Object, permette di ricercare in base a un Owner, le coppie (id,name) associate a un Pet presenti nel database.
     * @param  id di un Owner
     * @return una mappa di coppie (id, name) dei Pet presenti nel database in base all'owner associato.
     */
    @Override
    public Map<String, String> searchPetsByOwner(String id) {
        Map<String, String> listPets = new HashMap<>();
        String sqlSearchById = " SELECT * FROM masterdata" +
                "        INNER JOIN pet ON pet.id = masterdata.id" +
                "        WHERE pet.OWNER = ?";
        try {
            PreparedStatement statement = this.connection_db.getConnectData().prepareStatement(sqlSearchById);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                listPets.put(rs.getString("id"), rs.getString("name"));
            }
            return listPets;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'AppointmentDAO': {@link AppointmentDAO}  Data Access Object, permette di ricercare le le coppie (id,fiscalcode) associate a un Doctor presenti nel database.
     *
     * @return una mappa di coppie (id, fiscalcode) degli Doctor presenti nel database.
     */
    @Override
    public Map<String, String> searchAllDoctorByFiscalCod() {
        Map<String, String> dictionary = new HashMap<>();  //<key,value>
        String sqlSearch = "SELECT * FROM masterdata" +
                "                  INNER JOIN person" +
                "                             ON person.id = masterdata.id" +
                "                  INNER JOIN DOCTOR" +
                "                             ON  person.id = DOCTOR.ID";

        try {
            PreparedStatement statement = this.connection_db.getConnectData().prepareStatement(sqlSearch);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                dictionary.put(rs.getString("id"), rs.getString("fiscalcode"));
            }
            //dictionary.entrySet().forEach(System.out::println);
            return dictionary;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Metodo ereditato dall'interfaccia 'AppointmentDAO': {@link AppointmentDAO}  Data Access Object, permette di ricercare la specializzazione di un Doctor presente nel database.
     *
     * @return la specializzazione di un Doctor presente nel database.
     */
    @Override
    public String searchSpecializationByDoctor(String idDoctorSearched) {
        String specializationSearched = "";
        String sqlSearch = "SELECT * FROM masterdata" +
                "                  INNER JOIN person" +
                "                             ON person.id = masterdata.id" +
                "                  INNER JOIN DOCTOR" +
                "                             ON  person.id = DOCTOR.ID WHERE DOCTOR.ID = ? ";
        try {
            PreparedStatement statement = this.connection_db.getConnectData().prepareStatement(sqlSearch);
            statement.setString(1, idDoctorSearched);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                specializationSearched = rs.getString("specialization");
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
            PreparedStatement statement = this.connection_db.getConnectData().prepareStatement(sqlSearch);
            statement.setString(1, date);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {

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
            PreparedStatement statement = this.connection_db.getConnectData().prepareStatement(sqlSearch);
            statement.setString(1, date);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                countDayVisits = rs.getInt("count_visit");
            }
            //System.out.println(countDayVisits);
            return countDayVisits;
        } catch (SQLException e) {
            e.printStackTrace();
            return countDayVisits;
        }
    }

    /**
     * Prende come argomento un appuntamento
     * e deve ritornare l'id memorizzato nel db dell'appuntamento
     */

    @Override
    public String search(Appointment appointment) {
        try {
            PreparedStatement statement = connection_db.getConnectData().prepareStatement("SELECT * FROM BOOKING" +
                    "   WHERE BOOKING.DATE_VISIT =" + "'" + appointment.getLocalDate() + "'" +
                    "    AND BOOKING.TIME_START =" + "'" + appointment.getLocalTimeStart() + "'" +
                    "    AND BOOKING.TIME_END =" + "'" + appointment.getLocalTimeEnd() + "'" +
                    "    AND BOOKING.ID_PET =" + "'" + appointment.getId_pet() + "'" +
                    "    AND BOOKING.ID_DOCTOR =" + "'" + appointment.getId_doctor() + "'" +
                    "    AND BOOKING.ID_OWNER =" + "'" + appointment.getId_owner() + "'"
            );

            ResultSet rs = statement.executeQuery();
            String id_searched = "";
            if (rs.next()) {
                id_searched = rs.getString("id");
                return id_searched;
            } else {
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
    public String searchEmailOwnerbyIdAppointment(String id) {
        try {
            PreparedStatement statement = connection_db.getConnectData().prepareStatement("""
                    SELECT EMAIL FROM PERSON
                                      INNER JOIN OWNER
                                                 ON PERSON.id = OWNER.id
                                      INNER JOIN BOOKING
                                                 ON  OWNER.ID = BOOKING.ID_OWNER
                     WHERE BOOKING.ID = ?""");
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            String emailOwner = "";
            if (rs.next()) {
                emailOwner = rs.getString("email");
                return emailOwner;
            } else {
                JOptionPane.showMessageDialog(null, "Errore impossibile trovare l'email associata!");
                return emailOwner;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return null;
        }
    }

    @Override
    public Pet searchPetById(String id) {
        Pet pet = null;
        String sqlSearch = "SELECT * FROM masterdata" +
                "                  INNER JOIN PET" +
                "                             ON MASTERDATA.ID = ?";

        try {
            PreparedStatement statement = connection_db.getConnectData().prepareStatement(sqlSearch);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                pet = new Pet.Builder<>()
                        .addName(rs.getString("name"))
                        .addSurname(rs.getString("surname"))
                        .addSex(Gender.valueOf(rs.getString("sex")))
                        .addDateBirth(LocalDate.parse(rs.getString("datebirth")))
                        .setId_petRace(rs.getString("typepet"))
                        .setId_owner(rs.getString("owner"))
                        .setParticularSign(rs.getString("particularsign"))
                        .build();

            }
            assert pet != null;
            return pet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Owner searchOwnerById(String id) {
        Owner owner = null;
        String sqlSearch = "SELECT * FROM masterdata" +
                "                  INNER JOIN person" +
                "                             ON person.id = masterdata.id" +
                "                  INNER JOIN OWNER" +
                "                             ON  OWNER.id = MASTERDATA.ID WHERE OWNER.id = ?";
        try {
            PreparedStatement statement = connection_db.getConnectData().prepareStatement(sqlSearch);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                owner = new Owner.Builder<>()
                        .addName(rs.getString("name"))
                        .addSurname(rs.getString("surname"))
                        .addSex(Gender.valueOf(rs.getString("sex")))
                        .addDateBirth(LocalDate.parse(rs.getString("datebirth")))
                        .addAddress(rs.getString("address"))
                        .addCity(rs.getString("city"))
                        .addTelephone(rs.getString("telephone"))
                        .addFiscalCode(rs.getString("fiscalcode"))
                        .addEmail(rs.getString("email"))
                        .build();

            }
            assert owner != null;
            //System.out.println(owner.toString());
            return owner;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean searchIfExistAppointmentInReport(String id) {
        String sqlSearch = "SELECT * FROM REPORT WHERE ID_BOOKING = ? ";
        try {
            PreparedStatement statement = connection_db.getConnectData().prepareStatement(sqlSearch);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public List<Appointment> searchVisitbyDoctorAndDate(String idDoctorSearched, String date) {
        String sqlSearch = "SELECT * From BOOKING Where ID_DOCTOR = ? AND DATE_VISIT = ?";
        List<Appointment> listAppointment = new ArrayList<>();
        try {
            PreparedStatement statement = connection_db.getConnectData().prepareStatement(sqlSearch);
            statement.setString(1, idDoctorSearched);
            statement.setString(2, date);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Appointment p = new Appointment.Builder()
                        .setLocalDate(LocalDate.parse(rs.getString("date_visit")))
                        .setLocalTimeStart(LocalTime.parse(rs.getString("time_start")))
                        .setLocalTimeEnd(LocalTime.parse(rs.getString("time_end")))
                        .setId_doctor(rs.getString("id_doctor"))
                        .setSpecialitation(rs.getString("specialization"))
                        .setId_owner(rs.getString("id_owner"))
                        .setId_pet(rs.getString("id_pet"))
                        .build();
                listAppointment.add(p);
            }
            return listAppointment;
        } catch (SQLException e) {
            e.printStackTrace();
            return listAppointment;
        }
    }

    @Override
    public Doctor searchDoctorById(String id) {
        Doctor doctor = null;
        String sqlSearch = "SELECT * FROM masterdata" +
                "                  INNER JOIN person" +
                "                             ON person.id = masterdata.id" +
                "                  INNER JOIN DOCTOR" +
                "                             ON  DOCTOR.id = MASTERDATA.ID WHERE DOCTOR.id = ?";
        try {
            PreparedStatement statement = connection_db.getConnectData().prepareStatement(sqlSearch);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                doctor = new Doctor.Builder<>()
                        .addName(rs.getString("name"))
                        .addSurname(rs.getString("surname"))
                        .addSex(Gender.valueOf(rs.getString("sex")))
                        .addDateBirth(LocalDate.parse(rs.getString("datebirth")))
                        .addAddress(rs.getString("address"))
                        .addCity(rs.getString("city"))
                        .addTelephone(rs.getString("telephone"))
                        .addFiscalCode(rs.getString("fiscalcode"))
                        .addEmail(rs.getString("email"))
                        .addSpecialization(rs.getString("specialization"))
                        .addUsername(rs.getString("username"))
                        .addPassword(rs.getString("password"))
                        .build();

            }
            assert doctor != null;
            return doctor;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer countAppointmentsByDateAndDoctor(String date, String id_doctor) {
        List<Appointment> listAppointment = searchVisitbyDoctorAndDate(id_doctor, date);
        return listAppointment.size();
    }

    @Override
    public List<Appointment> searchVisitBeforeDate(LocalDate date, LocalTime time) {
        List<Appointment> listAllAppointment = findAll();
        //listAllAppointment.forEach(System.out::println);
        if (!listAllAppointment.isEmpty()) {
            return listAllAppointment.stream()
                    .filter(item -> (item.getLocalDate().isBefore(date) || (item.getLocalDate().isEqual(date) && item.getLocalTimeEnd().isBefore(time))))
                    .collect(Collectors.toList());
        } else {
            JOptionPane.showMessageDialog(null, "La ricerca è vuota!");
            return null;
        }
    }

    @Override
    public List<Appointment> searchVisitAfterDate(LocalDate date, LocalTime time) {
        List<Appointment> listAllAppointment = findAll();
        if (!listAllAppointment.isEmpty()) {
            return listAllAppointment.stream()
                    .filter(item -> (item.getLocalDate().isAfter(date) || (item.getLocalDate().isEqual(date) && item.getLocalTimeEnd().isAfter(time))))
                    .collect(Collectors.toList());
        } else {
            JOptionPane.showMessageDialog(null, "La ricerca è vuota!");
            return null;
        }
    }

    @Override
    public List<Appointment> searchVisitByDoctorBeforeDate(LocalDate date, LocalTime now, String id_doctor) {
        List<Appointment> listAllAppointment = searchVisitbyDoctorAndDate(id_doctor, date.toString());
        //listAllAppointment.forEach(System.out::println);
        if (!listAllAppointment.isEmpty()) {
            return listAllAppointment.stream()
                    .filter(item -> (item.getLocalDate().isBefore(date) || (item.getLocalDate().isEqual(date) && item.getLocalTimeEnd().isBefore(now))))
                    .collect(Collectors.toList());
        } else {
            JOptionPane.showMessageDialog(null, "La ricerca è vuota!");
            return null;
        }
    }

    @Override
    public List<Appointment> searchVisitbyDoctorOrPetAndDate(String idDoctorSearched, String id_pet, String date) {
        String sqlSearch = "SELECT * From BOOKING Where (ID_DOCTOR = ? OR ID_PET = ?) AND DATE_VISIT = ?";
        List<Appointment> listAppointment = new ArrayList<>();
        try {
            PreparedStatement statement = connection_db.getConnectData().prepareStatement(sqlSearch);
            statement.setString(1, idDoctorSearched);
            statement.setString(2, id_pet);
            statement.setString(3, date);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Appointment p = new Appointment.Builder()
                        .setLocalDate(LocalDate.parse(rs.getString("date_visit")))
                        .setLocalTimeStart(LocalTime.parse(rs.getString("time_start")))
                        .setLocalTimeEnd(LocalTime.parse(rs.getString("time_end")))
                        .setId_doctor(rs.getString("id_doctor"))
                        .setSpecialitation(rs.getString("specialization"))
                        .setId_owner(rs.getString("id_owner"))
                        .setId_pet(rs.getString("id_pet"))
                        .build();
                listAppointment.add(p);
            }
            return listAppointment;
        } catch (SQLException e) {
            e.printStackTrace();
            return listAppointment;
        }
    }

    @Override
    public List<Appointment> findAllVisitPet(String name, String surname) {
        String sqlSearchVisitsbyPet = """
                SELECT  *  FROM MASTERDATA INNER JOIN PET ON MASTERDATA.id = PET.id
                INNER JOIN BOOKING on PET.ID = BOOKING.ID_PET
                WHERE MASTERDATA.NAME = ? AND MASTERDATA.SURNAME = ?""";
        List<Appointment> listItems = new ArrayList<>();
        try {
            PreparedStatement statement = connection_db.getConnectData()
                    .prepareStatement(sqlSearchVisitsbyPet);
            statement.setString(1, name);
            statement.setString(2, surname);
            ResultSet r = statement.executeQuery();
            while (r.next()) {
                listItems.add(new Appointment.Builder()
                        .setId_pet(r.getString("id_pet"))
                        .setId_owner(r.getString("id_owner"))
                        .setSpecialitation(r.getString("specialization"))
                        .setId_doctor(r.getString("id_doctor"))
                        .setLocalDate(LocalDate.parse(r.getString("date_visit")))
                        .setLocalTimeStart(LocalTime.parse(r.getString("time_start")))
                        .setLocalTimeEnd(LocalTime.parse(r.getString("time_end")))
                        .build()
                );
            }
            //listItems.forEach(System.out::println);
            return listItems;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Appointment> findAllVisitPetBeforeDate(String name, String surname, LocalDate date) {
        List<Appointment> listAllAppointment = findAllVisitPet(name, surname);
        if (!listAllAppointment.isEmpty()) {
            return listAllAppointment.stream()
                    .filter(item -> (item.getLocalDate().isBefore(date)))
                    .collect(Collectors.toList());
        } else {
            JOptionPane.showMessageDialog(null, "La ricerca è vuota!");
            return null;
        }
    }

    @Override
    public List<Appointment> findAllVisitPetAfterDate(String name, String surname, LocalDate date) {
        List<Appointment> listAllAppointment = findAllVisitPet(name, surname);
        if (!listAllAppointment.isEmpty()) {
            return listAllAppointment.stream()
                    .filter(item -> (item.getLocalDate().isAfter(date) || (item.getLocalDate().isEqual(date))))
                    .collect(Collectors.toList());
        } else {
            JOptionPane.showMessageDialog(null, "La ricerca è vuota!");
            return null;
        }
    }

    @Override
    public List<Appointment> findAllVisitPetByID(String id) {
        String sqlSearchVisitsbyPet = """
                SELECT  *  FROM MASTERDATA INNER JOIN PET ON MASTERDATA.id = PET.id
                INNER JOIN BOOKING on PET.ID = BOOKING.ID_PET
                WHERE MASTERDATA.ID = ?""";
        List<Appointment> listItems = new ArrayList<>();
        try {
            PreparedStatement statement = connection_db.getConnectData()
                    .prepareStatement(sqlSearchVisitsbyPet);
            statement.setString(1, id);
            ResultSet r = statement.executeQuery();
            while (r.next()) {
                listItems.add(new Appointment.Builder()
                        .setId_pet(r.getString("id_pet"))
                        .setId_owner(r.getString("id_owner"))
                        .setSpecialitation(r.getString("specialization"))
                        .setId_doctor(r.getString("id_doctor"))
                        .setLocalDate(LocalDate.parse(r.getString("date_visit")))
                        .setLocalTimeStart(LocalTime.parse(r.getString("time_start")))
                        .setLocalTimeEnd(LocalTime.parse(r.getString("time_end")))
                        .build()
                );
            }
            return listItems;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Appointment> findAllVisitPetAfterDateByID(String id, LocalDate date) {
        List<Appointment> listAllAppointment = findAllVisitPetByID(id);
        if (!listAllAppointment.isEmpty()) {
            return listAllAppointment.stream()
                    .filter(item -> (item.getLocalDate().isAfter(date) || (item.getLocalDate().isEqual(date))))
                    .collect(Collectors.toList());
        } else {
            JOptionPane.showMessageDialog(null, "La ricerca è vuota!");
            return null;
        }
    }


}
