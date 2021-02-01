package dao;

import datasource.ConnectionDBH2;
import model.*;
import util.gui.Common;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe che implementa i metodi dell'interfaccia 'DoctorDAO': {@link DoctorDAO}  Data Access Object,  richiama i metodi dell'interfaccia 'Common': {@link Common}.
 * Serve a dialogare concretamente con il database.
 */
public class ConcreteDoctorDAO implements DoctorDAO, Common {
    private final ConnectionDBH2 connection_db;

    /**
     * Metodo costruttore
     *
     * @param connection_db instanza della connessione al database.
     */
    public ConcreteDoctorDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

    /**
     * Metodo ereditato dall'interfaccia 'DoctorDAO': {@link DoctorDAO}  Data Access Object, permette di aggiungere un nuovo membro Doctor nel database.
     *
     * @param doctor oggetto di tipo Doctor {@link Doctor} da aggiungere nel database.
     */
    @Override
    public void add(Doctor doctor) {
        PreparedStatement ps;
        try {
            ps = connection_db.getConnectData().prepareStatement("insert into masterdata(id, name, surname,sex, datebirth) values(?,?,?,?,?)");
            ps.setString(1, doctor.getId());
            ps.setString(2, doctor.getName());
            ps.setString(3, doctor.getSurname());
            ps.setString(4, doctor.getSex().toString());
            ps.setString(5, doctor.getDatebirth().toString());
            ps.executeUpdate();
            //System.out.println("Anagrafica Doctor aggiunta al DB!");

            ps = connection_db.getConnectData().prepareStatement("insert into person(id, address, city, telephone, email, FISCALCODE) values(?,?,?,?,?,?)");
            ps.setString(1, doctor.getId());
            ps.setString(2, doctor.getAddress());
            ps.setString(3, doctor.getCity());
            ps.setString(4, doctor.getTelephone());
            ps.setString(5, doctor.getEmail());
            ps.setString(6, doctor.getFiscalCode());
            ps.executeUpdate();
            //System.out.println("Dati civici Doctor aggiunti al DB!");

            ps = connection_db.getConnectData().prepareStatement("insert into doctor(id, specialization, username, password) values(?,?,?,?)");
            ps.setString(1, doctor.getId());
            ps.setString(2, doctor.getSpecialization());
            ps.setString(3, doctor.getUsername());
            ps.setString(4, doctor.getPassword());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Dottore/essa aggiunto/a correttamente!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }

    }


    /**
     * Metodo ereditato dall'interfaccia 'DoctorDAO': {@link DoctorDAO}  Data Access Object, permette di ricercare tutti i membri Doctor presenti nel database.
     *
     * @return Lista di oggetti di tipo Doctor {@link Doctor} presenti nel database.
     */
    @Override
    public List<Doctor> findAll() {
        List<Doctor> listItems = new ArrayList<>();
        try {
            PreparedStatement statement = connection_db.getConnectData()
                    .prepareStatement(" SELECT * FROM masterdata INNER JOIN person ON person.id = masterdata.id INNER JOIN DOCTOR ON  person.id = DOCTOR.id ");
            ResultSet r = statement.executeQuery();
            while (r.next()) {
                listItems.add(new Doctor.Builder<>()
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

    /**
     * Metodo ereditato dall'interfaccia 'DoctorDAO': {@link DoctorDAO}  Data Access Object, permette di aggiornare il membro Doctor presente nel database.
     *
     * @param id     dell'oggetto Doctor {@link Doctor} da modificare nel database.
     * @param doctor oggetto Doctor che contiene i campi aggiornati da modificare nel database.
     */
    @Override
    public void update(String id, Doctor doctor) {
        String sqlMasterData = "UPDATE masterdata SET name = ?, surname = ?, sex = ?, datebirth = ? where masterdata.id = ?";
        String sqlPersonData = "UPDATE person SET address = ?, city = ?, telephone = ?, email = ?, fiscalcode = ? where person.id = ?";
        String sqlDoctorData = "UPDATE doctor SET specialization = ?, username = ?, password = ? where doctor.id = ?";
        PreparedStatement ps;
        try {
            ps = connection_db.getConnectData().prepareStatement(sqlMasterData);
            ps.setString(1, doctor.getName());
            ps.setString(2, doctor.getSurname());
            ps.setString(3, doctor.getSex().toString());
            ps.setString(4, doctor.getDatebirth().toString());
            ps.setString(5, id);
            ps.executeUpdate();

            //System.out.println("Aggiornati dati Anagrafica del doctor!");

            ps = connection_db.getConnectData().prepareStatement(sqlPersonData);
            ps.setString(1, doctor.getAddress());
            ps.setString(2, doctor.getCity());
            ps.setString(3, doctor.getTelephone());
            ps.setString(4, doctor.getEmail());
            ps.setString(5, doctor.getFiscalCode());
            ps.setString(6, id);
            ps.executeUpdate();
            //System.out.println("Aggiornati dati persona del doctor!");

            ps = connection_db.getConnectData().prepareStatement(sqlDoctorData);
            ps.setString(1, doctor.getSpecialization());
            ps.setString(2, doctor.getUsername());
            ps.setString(3, doctor.getPassword());
            ps.setString(4, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Modificato correttamente!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'DoctorDAO': {@link DoctorDAO}  Data Access Object, permette di cancellare il membro Doctor presente nel database,
     *
     * @param id del membro Doctor {@link Doctor} da cancellare nel database.
     */
    @Override
    public void delete(String id) {
        //System.out.println("id da cancellare a cascata: " + id);

        PreparedStatement ps;
        try {
            ps = connection_db.getConnectData().prepareStatement("delete from masterdata where masterdata.id = " + "'" + id + "'");
            ps.executeUpdate();
            //System.out.println("Cancellati dati Anagrafica del doctor!");

            ps = connection_db.getConnectData().prepareStatement("delete from person where person.id = " + "'" + id + "'");

            ps.executeUpdate();
            //System.out.println("Cancellati dati civici del doctor!");

            ps = connection_db.getConnectData().prepareStatement("delete from DOCTOR where DOCTOR.id = " + "'" + id + "'");

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cancellato correttamente!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'DoctorDAO': {@link DoctorDAO}  Data Access Object, permette di ricercare
     * tutte le specializzazioni presenti sul database.
     *
     * @return lista di tutte le specializzazioni presenti nel database.
     */
    @Override
    public List<String> searchAllSpecialization() {
        List<String> listSpecialization = new ArrayList<>();

        String sqlSearchSpecialization = "SELECT * FROM SPECIALIZATION";

        try {
            PreparedStatement statement = this.connection_db.getConnectData().prepareStatement(sqlSearchSpecialization);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("name"));
                listSpecialization.add(rs.getString("name"));
            }
            return listSpecialization;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'DoctorDAO': {@link DoctorDAO}  Data Access Object, permette di ricercare
     * il membro Doctor, se presente nel database, e ritorna l'id del membro Doctor ricercato. Se non è presente ritorna una stringa vuota id="".
     *
     * @param doctor il membro Doctor {@link Doctor} da ricercare nel database.
     * @return id del membro Doctor ricercato. Se non è presente ritorna una stringa vuota id="".
     */
    @Override
    public String search(Doctor doctor) {
        try {
            PreparedStatement statement = connection_db.getConnectData().prepareStatement("SELECT * FROM masterdata" +
                    "    INNER JOIN person" +
                    "    ON person.id = masterdata.id" +
                    "    INNER JOIN DOCTOR  " +
                    "    ON  person.id = DOCTOR.id WHERE masterdata.name = ? AND masterdata.surname = ? " +
                    "    AND masterdata.sex = ?" +
                    "    AND masterdata.datebirth = ? AND PERSON.FISCALCODE = ?");
            statement.setString(1, doctor.getName());
            statement.setString(2, doctor.getSurname());
            statement.setString(3, doctor.getSex().toString());
            statement.setString(4, doctor.getDatebirth().toString());
            statement.setString(5, doctor.getFiscalCode());
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

    /**
     * Metodo ereditato dall'interfaccia 'DoctorDAO': {@link DoctorDAO}  Data Access Object, permette di ricercare
     * il membro Doctor, a partire dall'username e password, se presente nel database, e ritorna l'oggetto Doctor ricercato. Se non è presente ritorna null.
     *
     * @param userLogged utente loggato di tipo {@link User} da ricercare nel database.
     * @return oggetto di tipo Doctor {@link Doctor} ricercato. Se non è presente ritorna null.
     */
    @Override
    public Doctor searchByUsernameAndPassword(User userLogged) {
        PreparedStatement ps;
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
            if (r.next()) {
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
            } else {
                searchEmpty();
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return null;
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'DoctorDAO': {@link DoctorDAO}  Data Access Object, permette di ricercare
     * i dati del membro Doctor, a partire dall'id, se presente nel database, e ritorna l'oggetto Doctor ricercato. Se non è presente ritorna null.
     *
     * @param id del membro Doctor {@link Doctor} da ricercare nel database.
     * @return oggetto di tipo Doctor {@link Doctor} ricercato. Se non è presente ritorna null.
     */
    @Override
    public Doctor searchById(String id) {
        String sqlSearch = "SELECT * FROM masterdata" +
                "    INNER JOIN person" +
                "    ON person.id = masterdata.id" +
                "    INNER JOIN DOCTOR  " +
                "    ON  person.id = DOCTOR.id WHERE DOCTOR.id = ? ";
        try {
            PreparedStatement ps = connection_db.getConnectData().prepareStatement(sqlSearch);
            ps.setString(1, id);
            ResultSet r = ps.executeQuery();
            if (r.next()) {
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
            } else {
                searchEmpty();
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return null;
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'DoctorDAO': {@link DoctorDAO}  Data Access Object, permette di controllare se l'oggetto Doctor passato a parametro non è duplicato nel database.
     * Se non è duplicato restituisce True, altrimenti False.
     *
     * @param doctor oggetto Doctor {@link Doctor} da ricercare nel database.
     * @return true se l'oggetto doctor non è duplicato, false altrimenti.
     */
    @Override
    public boolean isNotDuplicate(Doctor doctor) {
        try {
            PreparedStatement statement = connection_db.getConnectData().prepareStatement("SELECT * FROM masterdata" +
                    "    INNER JOIN person" +
                    "    ON person.id = masterdata.id" +
                    "    INNER JOIN  DOCTOR  " +
                    "    ON  person.id = DOCTOR.id WHERE masterdata.name = ? AND masterdata.surname = ? " +
                    "    AND masterdata.sex = ?" +
                    "    AND masterdata.datebirth = ? AND PERSON.FISCALCODE = ? AND PERSON.TELEPHONE= ? " +
                    "    AND PERSON.CITY =? AND PERSON.ADDRESS = ? " +
                    "    AND PERSON.EMAIL =? AND DOCTOR.USERNAME =? AND DOCTOR.SPECIALIZATION =?");
            statement.setString(1, doctor.getName());
            statement.setString(2, doctor.getSurname());
            statement.setString(3, doctor.getSex().toString());
            statement.setString(4, doctor.getDatebirth().toString());
            statement.setString(5, doctor.getFiscalCode());
            statement.setString(6, doctor.getTelephone());
            statement.setString(7, doctor.getCity());
            statement.setString(8, doctor.getAddress());
            statement.setString(9, doctor.getEmail());
            statement.setString(10, doctor.getUsername());
            statement.setString(11, doctor.getSpecialization());
            ResultSet rs = statement.executeQuery();
            String id_searched = "";
            if (rs.next()) {
                id_searched = rs.getString("id");
            }
            return id_searched.equals(""); //true -->non è duplicato
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'DoctorDAO': {@link DoctorDAO}  Data Access Object, permette di aggiornare la password dell'utente Doctor.
     *
     * @param id  del membro Doctor {@link Doctor} da ricercare nel database, e di cui aggiornare la password.
     * @param pwd nuova password del membro Doctor {@link Doctor} da aggiornare nel database.
     */
    @Override
    public void updatePassword(String id, String pwd) {
        String sqlSearch = "UPDATE DOCTOR" +
                " SET DOCTOR.PASSWORD = ? WHERE DOCTOR.ID = ? ";
        try {
            PreparedStatement ps = connection_db.getConnectData().prepareStatement(sqlSearch);
            ps.setString(1, pwd);
            ps.setString(2, id);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Password Aggiornata!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }
}
