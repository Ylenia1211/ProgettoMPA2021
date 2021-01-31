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
 * Classe che implementa i metodi dell'interfaccia 'SecretariatDAO': {@link SecretariatDAO}  Data Access Object.
 * Serve a dialogare concretamente con il database.
 */
public class ConcreteSecretariatDAO implements SecretariatDAO, Common {

    private final ConnectionDBH2 connection_db;

    /**
     * Metodo costruttore
     *
     * @param connection_db instanza della connessione al database.
     */
    public ConcreteSecretariatDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

    /**
     * Metodo ereditato dall'interfaccia 'SecretariatDAO': {@link SecretariatDAO}  Data Access Object, permette di aggiungere un nuovo membro Secretariat nel database.
     *
     * @param secretariat oggetto di tipo Secretariat {@link Secretariat} da aggiungere nel database.
     */
    @Override
    public void add(Secretariat secretariat) {
        PreparedStatement ps;
        try {
            ps = connection_db.getConnectData().prepareStatement("insert into masterdata(id, name, surname,sex, datebirth) values(?,?,?,?,?)");
            ps.setString(1, secretariat.getId());
            ps.setString(2, secretariat.getName());
            ps.setString(3, secretariat.getSurname());
            ps.setString(4, secretariat.getSex().toString());
            ps.setString(5, secretariat.getDatebirth().toString());
            ps.executeUpdate();
            System.out.println("Anagrafica secretariat aggiunta al DB!");

            ps = connection_db.getConnectData().prepareStatement("insert into person(id, address, city, telephone, email, FISCALCODE) values(?,?,?,?,?,?)");
            ps.setString(1, secretariat.getId());
            ps.setString(2, secretariat.getAddress());
            ps.setString(3, secretariat.getCity());
            ps.setString(4, secretariat.getTelephone());
            ps.setString(5, secretariat.getEmail());
            ps.setString(6, secretariat.getFiscalCode());
            ps.executeUpdate();
            System.out.println("Dati civici secretariat aggiunti al DB!");

            ps = connection_db.getConnectData().prepareStatement("insert into SECRETARIAT(id, username, password) values(?,?,?)");
            ps.setString(1, secretariat.getId());
            ps.setString(2, secretariat.getUsername());
            ps.setString(3, secretariat.getPassword());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "secretariat aggiunto correttamente!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'SecretariatDAO': {@link SecretariatDAO}  Data Access Object, permette di ricercare tutti i membri Secretariat presenti nel database.
     *
     * @return Lista di oggetti di tipo Secretariat {@link Secretariat} presenti nel database.
     */
    @Override
    public List<Secretariat> findAll() {
        List<Secretariat> listItems = new ArrayList<>();
        try {
            PreparedStatement statement = connection_db.getConnectData()
                    .prepareStatement(" SELECT * FROM masterdata INNER JOIN person ON person.id = masterdata.id INNER JOIN SECRETARIAT ON  person.id = SECRETARIAT.id ");
            ResultSet r = statement.executeQuery();
            while (r.next()) {
                listItems.add(new Secretariat.Builder<>()
                        .addName(r.getString("name"))
                        .addSurname(r.getString("surname"))
                        .addSex(Gender.valueOf(r.getString("sex")))
                        .addDateBirth(LocalDate.parse(r.getString("datebirth")))
                        .addFiscalCode(r.getString("fiscalcode"))
                        .addAddress(r.getString("address"))
                        .addCity(r.getString("city"))
                        .addTelephone(r.getString("telephone"))
                        .addEmail(r.getString("email"))
                        .addUsername(r.getString("username"))
                        .addPassword(r.getString("password"))
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
     * Metodo ereditato dall'interfaccia 'SecretariatDAO': {@link SecretariatDAO}  Data Access Object, permette di aggiornare il membro Secretariat presente nel database.
     *
     * @param id          dell'oggetto Secretariat {@link Secretariat} da modificare nel database.
     * @param secretariat oggetto Secretariat che contiene i campi aggiornati da modificare nel database.
     */
    @Override
    public void update(String id, Secretariat secretariat) {
        String sqlMasterData = "UPDATE masterdata SET name = ?, surname = ?, sex = ?, datebirth = ? where masterdata.id = ?";
        String sqlPersonData = "UPDATE person SET address = ?, city = ?, telephone = ?, email = ?, fiscalcode = ? where person.id = ?";
        String sqlSecretariatData = "UPDATE SECRETARIAT SET  username = ?, password = ? where SECRETARIAT.id = ?";
        PreparedStatement ps;
        try {
            ps = connection_db.getConnectData().prepareStatement(sqlMasterData);
            ps.setString(1, secretariat.getName());
            ps.setString(2, secretariat.getSurname());
            ps.setString(3, secretariat.getSex().toString());
            ps.setString(4, secretariat.getDatebirth().toString());
            ps.setString(5, id);
            ps.executeUpdate();

            System.out.println("Aggiornati dati Anagrafica del secretariat!");

            ps = connection_db.getConnectData().prepareStatement(sqlPersonData);
            ps.setString(1, secretariat.getAddress());
            ps.setString(2, secretariat.getCity());
            ps.setString(3, secretariat.getTelephone());
            ps.setString(4, secretariat.getEmail());
            ps.setString(5, secretariat.getFiscalCode());
            ps.setString(6, id);
            ps.executeUpdate();
            System.out.println("Aggiornati dati persona del secretariat!");

            ps = connection_db.getConnectData().prepareStatement(sqlSecretariatData);
            ps.setString(1, secretariat.getUsername());
            ps.setString(2, secretariat.getPassword());
            ps.setString(3, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Modificato correttamente!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'SecretariatDAO': {@link SecretariatDAO}  Data Access Object, permette di cancellare il membro Secretariat presente nel database,
     *
     * @param id del membro Secretariat {@link Secretariat} da cancellare nel database.
     */
    @Override
    public void delete(String id) {
        //System.out.println("id da cancellare a cascata: " + id);
        PreparedStatement ps;
        try {
            ps = connection_db.getConnectData().prepareStatement("delete from masterdata where masterdata.id = " + "'" + id + "'");
            ps.executeUpdate();
            System.out.println("Cancellati dati Anagrafica del secretariat!");
            ps = connection_db.getConnectData().prepareStatement("delete from person where person.id = " + "'" + id + "'");

            ps.executeUpdate();
            System.out.println("Cancellati dati civici del secretariat!");

            ps = connection_db.getConnectData().prepareStatement("delete from SECRETARIAT where SECRETARIAT.id = " + "'" + id + "'");
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cancellato correttamente!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'SecretariatDAO': {@link SecretariatDAO}  Data Access Object, permette di ricercare
     * il membro Secretariat, se presente nel database, e ritorna l'id del membro Secretatiat ricercato. Se non è presente ritorna una stringa vuota id="".
     *
     * @param secretariat il membro Secretariat {@link Secretariat} da ricercare nel database.
     * @return id del membro Secretariat ricercato. Se non è presente ritorna una stringa vuota id="".
     */
    @Override
    public String search(Secretariat secretariat) {
        try {
            PreparedStatement statement = connection_db.getConnectData().prepareStatement("SELECT * FROM masterdata" +
                    "    INNER JOIN person" +
                    "    ON person.id = masterdata.id" +
                    "    INNER JOIN SECRETARIAT  " +
                    "    ON  person.id = SECRETARIAT.id WHERE masterdata.name = ? AND masterdata.surname = ? " +
                    "    AND masterdata.sex = ?" +
                    "    AND masterdata.datebirth = ? AND PERSON.FISCALCODE = ?");
            statement.setString(1, secretariat.getName());
            statement.setString(2, secretariat.getSurname());
            statement.setString(3, secretariat.getSex().toString());
            statement.setString(4, secretariat.getDatebirth().toString());
            statement.setString(5, secretariat.getFiscalCode());
            ResultSet rs = statement.executeQuery();
            String id_searched = "";
            if (rs.next()) {
                id_searched = rs.getString("id");
            } else {
                JOptionPane.showMessageDialog(null, "Ricerca Vuota");
            }
            return id_searched;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return null;
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'SecretariatDAO': {@link SecretariatDAO}  Data Access Object, permette di ricercare
     * il membro Secretariat, a partire dall'username e password, se presente nel database, e ritorna l'oggetto Secretatiat ricercato. Se non è presente ritorna null.
     *
     * @param userLogged utente loggato di tipo {@link User} da ricercare nel database.
     * @return oggetto di tipo Secretariat {@link Secretariat} ricercato. Se non è presente ritorna null.
     */
    @Override
    public Secretariat searchByUsernameAndPassword(User userLogged) {
        PreparedStatement ps;
        String sqlSearch = "SELECT * FROM masterdata" +
                "    INNER JOIN person" +
                "    ON person.id = masterdata.id" +
                "    INNER JOIN SECRETARIAT  " +
                "    ON  person.id = SECRETARIAT.id WHERE SECRETARIAT.USERNAME = ? AND SECRETARIAT.PASSWORD = ? ";
        try {
            ps = connection_db.getConnectData().prepareStatement(sqlSearch);
            ps.setString(1, userLogged.getUsername());
            ps.setString(2, userLogged.getPassword());
            ResultSet r = ps.executeQuery();
            if (r.next()) {
                return new Secretariat.Builder<>()
                        .addName(r.getString("name"))
                        .addSurname(r.getString("surname"))
                        .addSex(Gender.valueOf(r.getString("sex")))
                        .addDateBirth(LocalDate.parse(r.getString("datebirth")))
                        .addFiscalCode(r.getString("fiscalcode"))
                        .addAddress(r.getString("address"))
                        .addCity(r.getString("city"))
                        .addTelephone(r.getString("telephone"))
                        .addEmail(r.getString("email"))
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
     * Metodo ereditato dall'interfaccia 'SecretariatDAO': {@link SecretariatDAO}  Data Access Object, permette di controllare se l'oggetto Secretariat passato a parametro non è duplicato nel database.
     * Se non è duplicato restituisce True, altrimenti False.
     *
     * @param secretariat oggetto Secretariat {@link Secretariat} da ricercare nel database.
     * @return true se l'oggetto secretariat non è duplicato, false altrimenti.
     */
    @Override
    public boolean isNotDuplicate(Secretariat secretariat) {
        try {
            PreparedStatement statement = connection_db.getConnectData().prepareStatement("SELECT * FROM masterdata" +
                    "    INNER JOIN person" +
                    "    ON person.id = masterdata.id" +
                    "    INNER JOIN  SECRETARIAT  " +
                    "    ON  person.id = SECRETARIAT.id WHERE masterdata.name = ? AND masterdata.surname = ? " +
                    "    AND masterdata.sex = ?" +
                    "    AND masterdata.datebirth = ? AND PERSON.FISCALCODE = ? AND PERSON.TELEPHONE= ?" +
                    "    AND PERSON.CITY =? AND PERSON.ADDRESS = ? " +
                    "    AND PERSON.EMAIL =? AND SECRETARIAT.USERNAME =?");
            statement.setString(1, secretariat.getName());
            statement.setString(2, secretariat.getSurname());
            statement.setString(3, secretariat.getSex().toString());
            statement.setString(4, secretariat.getDatebirth().toString());
            statement.setString(5, secretariat.getFiscalCode());
            statement.setString(6, secretariat.getTelephone());
            statement.setString(7, secretariat.getCity());
            statement.setString(8, secretariat.getAddress());
            statement.setString(9, secretariat.getEmail());
            statement.setString(10, secretariat.getUsername());


            ResultSet rs = statement.executeQuery();
            String id_searched = "";
            if (rs.next()) {
                id_searched = rs.getString("id");
            }
            return id_searched.equals("");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'SecretariatDAO': {@link SecretariatDAO}  Data Access Object, permette di ricercare
     * i dati del membro Secretariat, a partire dall'id, se presente nel database, e ritorna l'oggetto Secretatiat ricercato. Se non è presente ritorna null.
     *
     * @param id del membro Secretariat {@link Secretariat} da ricercare nel database.
     * @return oggetto di tipo Secretariat {@link Secretariat} ricercato. Se non è presente ritorna null.
     */
    @Override
    public Secretariat searchById(String id) {
        String sqlSearch = "SELECT * FROM masterdata" +
                "    INNER JOIN person" +
                "    ON person.id = masterdata.id" +
                "    INNER JOIN SECRETARIAT " +
                "    ON  person.id = SECRETARIAT.id WHERE SECRETARIAT.id = ? ";
        try {
            PreparedStatement ps = connection_db.getConnectData().prepareStatement(sqlSearch);
            ps.setString(1, id);
            ResultSet r = ps.executeQuery();
            if (r.next()) {
                return new Secretariat.Builder<>()
                        .addName(r.getString("name"))
                        .addSurname(r.getString("surname"))
                        .addSex(Gender.valueOf(r.getString("sex")))
                        .addDateBirth(LocalDate.parse(r.getString("datebirth")))
                        .addFiscalCode(r.getString("fiscalcode"))
                        .addAddress(r.getString("address"))
                        .addCity(r.getString("city"))
                        .addTelephone(r.getString("telephone"))
                        .addEmail(r.getString("email"))
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
     * Metodo ereditato dall'interfaccia 'SecretariatDAO': {@link SecretariatDAO}  Data Access Object, permette di aggiornare la password dell'utente Secretariat.
     *
     * @param id  del membro Secretariat {@link Secretariat} da ricercare nel database, e di cui aggiornare la password.
     * @param pwd nuova password del membro Secretariat {@link Secretariat} da aggiornare nel database.
     */
    @Override
    public void updatePassword(String id, String pwd) {
        String sqlSearch = "UPDATE SECRETARIAT" +
                " SET SECRETARIAT.PASSWORD = ? WHERE SECRETARIAT.ID = ? ";
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
