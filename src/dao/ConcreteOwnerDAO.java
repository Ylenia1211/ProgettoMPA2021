package dao;

import datasource.ConnectionDBH2;
import model.Gender;
import model.Owner;
import model.Pet;

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
 * Classe che implementa i metodi dell'interfaccia 'OwnerDAO': {@link OwnerDAO}  Data Access Object.
 * Serve a dialogare concretamente con il database.
 */
public class ConcreteOwnerDAO implements OwnerDAO {
    private final ConnectionDBH2 connection_db;

    /**
     * Metodo costruttore
     *
     * @param connection_db instanza della connessione al database.
     */
    public ConcreteOwnerDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

    /**
     * Metodo ereditato dall'interfaccia 'OwnerDAO': {@link OwnerDAO}  Data Access Object, permette di aggiungere un nuovo Owner nel database.
     *
     * @param owner oggetto di tipo Owner {@link Owner} da aggiungere nel database.
     */
    @Override
    public void add(Owner owner) {

        PreparedStatement ps;
        try {
            ps = connection_db.getConnectData().prepareStatement("insert into masterdata(id, name, surname,sex, datebirth) values(?,?,?,?,?)");
            ps.setString(1, owner.getId());
            ps.setString(2, owner.getName());
            ps.setString(3, owner.getSurname());
            ps.setString(4, owner.getSex().toString());
            ps.setString(5, owner.getDatebirth().toString());
            ps.executeUpdate();
            //System.out.println("Anagrafica Owner aggiunta al DB!");

            ps = connection_db.getConnectData().prepareStatement("insert into person(id, address, city, telephone, email, fiscalcode) values(?,?,?,?,?,?)");
            ps.setString(1, owner.getId());
            ps.setString(2, owner.getAddress());
            ps.setString(3, owner.getCity());
            ps.setString(4, owner.getTelephone());
            ps.setString(5, owner.getEmail());
            ps.setString(6, owner.getFiscalCode());
            ps.executeUpdate();
            //System.out.println("Dati civici Owner aggiunti al DB!");

            ps = connection_db.getConnectData().prepareStatement("insert into owner(id, tot_animal) values(?,?)");
            ps.setString(1, owner.getId());
            ps.setInt(2, owner.getTot_animal());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cliente aggiunto correttamente!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }


    }

    /**
     * Metodo ereditato dall'interfaccia 'OwnerDAO': {@link OwnerDAO}  Data Access Object, permette di ricercare tutti gli Owner presenti nel database.
     *
     * @return Lista di oggetti di tipo Owner {@link Owner} presenti nel database.
     */
    @Override
    public List<Owner> findAll() {
        List<Owner> listItems = new ArrayList<>();
        try {
            PreparedStatement statement = connection_db.getConnectData()
                    .prepareStatement(" SELECT * FROM masterdata INNER JOIN person ON person.id = masterdata.id INNER JOIN owner ON  person.id = owner.id ");
            ResultSet r = statement.executeQuery();
            while (r.next()) {
                listItems.add(new Owner.Builder<>()
                        .addName(r.getString("name"))
                        .addSurname(r.getString("surname"))
                        .addSex(Gender.valueOf(r.getString("sex")))
                        .addDateBirth(LocalDate.parse(r.getString("datebirth")))
                        .addFiscalCode(r.getString("fiscalcode"))
                        .addAddress(r.getString("address"))
                        .addCity(r.getString("city"))
                        .addTelephone(r.getString("telephone"))
                        .addEmail(r.getString("email")).setTotAnimal(r.getInt("tot_animal")).build()
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
     * Metodo ereditato dall'interfaccia 'OwnerDAO': {@link OwnerDAO}  Data Access Object, permette di aggiornare l'Owner presente nel database.
     *
     * @param id     dell'Owner {@link Owner} da modificare nel database.
     * @param client Owner che contiene i campi aggiornati da modificare nel database.
     */
    @Override
    public void update(String id, Owner client) {
        String sqlMasterData = "UPDATE masterdata SET name = ?, surname = ?, sex = ?, datebirth = ? where masterdata.id = ?";
        String sqlPersonData = "UPDATE person SET address = ?, city = ?, telephone = ?, email = ?, fiscalcode = ? where person.id = ?";

        PreparedStatement ps;
        try {
            ps = connection_db.getConnectData().prepareStatement(sqlMasterData);
            ps.setString(1, client.getName());
            ps.setString(2, client.getSurname());
            ps.setString(3, client.getSex().toString());
            ps.setString(4, client.getDatebirth().toString());
            ps.setString(5, id);
            ps.executeUpdate();

            //System.out.println("Aggiornati dati Anagrafica del Owner!");

            ps = connection_db.getConnectData().prepareStatement(sqlPersonData);
            ps.setString(1, client.getAddress());
            ps.setString(2, client.getCity());
            ps.setString(3, client.getTelephone());
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getFiscalCode());
            ps.setString(6, id);
            ps.executeUpdate();
            //System.out.println("Aggiornati dati persona del Owner!");
            JOptionPane.showMessageDialog(null, "Cliente Modificato correttamente!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'OwnerDAO': {@link OwnerDAO}  Data Access Object, permette di cancellare l'Owner presente nel database,
     * e a cascata tutti  i Pet {@link Pet} e gli Appointment associati {@link model.Appointment}, richiamando il metodo delete del ConcretePetDao {@link ConcretePetDAO}.
     *
     * @param id dell'Owner {@link Owner} da cancellare nel database.
     */
    @Override
    public void delete(String id) {
        System.out.println("id da cancellare a cascata: " + id);
        List<Pet> pets;
        PreparedStatement ps;
        try {
            //prima di cancellare il l'owner devo cancellare anche tutti gli animali a lui associati
            ConcretePetDAO petDao = new ConcretePetDAO(ConnectionDBH2.getInstance());
            pets = petDao.searchByOwner(id);
            pets.forEach(pet -> petDao.delete(petDao.search(pet)));

            ps = connection_db.getConnectData().prepareStatement("delete from masterdata where masterdata.id = " + "'" + id + "'");
            ps.executeUpdate();
            //System.out.println("Cancellati dati Anagrafica del Owner!");
            ps = connection_db.getConnectData().prepareStatement("delete from person where person.id = " + "'" + id + "'");
            ps.executeUpdate();
            //System.out.println("Cancellati dati civici del Owner!");
            ps = connection_db.getConnectData().prepareStatement("delete from owner where owner.id = " + "'" + id + "'");
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Cliente Cancellato correttamente!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'OwnerDAO': {@link OwnerDAO}  Data Access Object, permette di ricercare
     * l'Owner se presente nel database, e ritorna l'id dell'Owner ricercato. Se non è presente ritorna una stringa vuota id="".
     *
     * @param client l'Owner {@link Owner} da ricercare nel database.
     * @return id dell'Owner ricercato. Se non è presente ritorna una stringa vuota id="".
     */
    @Override
    public String search(Owner client) {
        try {
            PreparedStatement statement = connection_db.getConnectData().prepareStatement("SELECT * FROM masterdata" +
                    "    INNER JOIN person" +
                    "    ON person.id = masterdata.id" +
                    "    INNER JOIN owner  " +
                    "    ON  person.id = owner.id WHERE masterdata.name = ? AND masterdata.surname = ? " +
                    "    AND masterdata.sex = ?" +
                    "    AND masterdata.datebirth = ? AND PERSON.FISCALCODE = ?");
            statement.setString(1, client.getName());
            statement.setString(2, client.getSurname());
            statement.setString(3, client.getSex().toString());
            statement.setString(4, client.getDatebirth().toString());
            statement.setString(5, client.getFiscalCode());
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
     * Metodo ereditato dall'interfaccia 'OwnerDAO': {@link OwnerDAO}  Data Access Object, permette di controllare se l'oggetto Owner passato a parametro non è duplicato nel database.
     * Se non è duplicato restituisce True, altrimenti False.
     *
     * @param owner l'Owner {@link Owner} da ricercare nel database.
     * @return true se l'oggetto owner non è duplicato, false altrimenti.
     */
    @Override
    public boolean isNotDuplicate(Owner owner) {
        try {
            PreparedStatement statement = connection_db.getConnectData().prepareStatement("SELECT * FROM masterdata" +
                    "    INNER JOIN person" +
                    "    ON person.id = masterdata.id" +
                    "    INNER JOIN owner  " +
                    "    ON  person.id = owner.id WHERE masterdata.name = ? AND masterdata.surname = ? " +
                    "    AND masterdata.sex = ?" +
                    "    AND masterdata.datebirth = ? AND PERSON.FISCALCODE = ? AND PERSON.TELEPHONE= ? AND PERSON.CITY = ? AND PERSON.ADDRESS = ?");
            statement.setString(1, owner.getName());
            statement.setString(2, owner.getSurname());
            statement.setString(3, owner.getSex().toString());
            statement.setString(4, owner.getDatebirth().toString());
            statement.setString(5, owner.getFiscalCode());
            statement.setString(6, owner.getTelephone());
            statement.setString(7, owner.getCity());
            statement.setString(8, owner.getAddress());
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
}
