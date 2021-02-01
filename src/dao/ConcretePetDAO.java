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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe che implementa i metodi dell'interfaccia 'PetDAO': {@link PetDAO}  Data Access Object.
 * Serve a dialogare concretamente con il database.
 */
public class ConcretePetDAO implements PetDAO {
    private final ConnectionDBH2 connection_db;

    /**
     * Metodo costruttore
     *
     * @param connection_db instanza della connessione al database.
     */
    public ConcretePetDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

    /**
     * Metodo ereditato dall'interfaccia 'PetDAO': {@link PetDAO}  Data Access Object, permette di aggiungere un nuovo Pet nel database.
     *
     * @param pet oggetto di tipo Pet {@link Pet} da aggiungere nel database.
     */
    @Override
    public void add(Pet pet) {

        try {
            PreparedStatement ps = connection_db.getConnectData().prepareStatement("insert into masterdata(id, name, surname,sex, datebirth) values(?,?,?,?,?)");
            ps.setString(1, pet.getId());
            ps.setString(2, pet.getName());
            ps.setString(3, pet.getSurname());
            ps.setString(4, pet.getSex().toString());
            ps.setString(5, pet.getDatebirth().toString());
            ps.executeUpdate();
            //System.out.println("Anagrafica Pet aggiunta al DB!");


            ps = connection_db.getConnectData().prepareStatement("insert into pet(id, typepet, owner, particularsign) values(?,?,?,?)");
            ps.setString(1, pet.getId());
            ps.setString(2, pet.getId_petRace());
            ps.setString(3, pet.getId_owner());
            ps.setString(4, pet.getParticularSign());

            ps.executeUpdate();
            //System.out.println("Dati personali Pet aggiunti al DB!");
            JOptionPane.showMessageDialog(null, "Paziente aggiunto correttamente!");


            //Dobbiamo cercare il numero di pazienti (pet) associati al cliente (owner) e incrementare il numero di pazienti associati
            String sqlSearchNumAnimalOwner = "SELECT tot_animal FROM owner WHERE owner.id = ?";
            int number_pet = 0;
            try {
                ps = connection_db.getConnectData().prepareStatement(sqlSearchNumAnimalOwner);
                ps.setString(1, pet.getId_owner());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    number_pet = rs.getInt("tot_animal");
                    System.out.println("numero pazienti associati adesso: " + number_pet);
                }
                number_pet += 1; //incrementa di 1 i pazienti associati al quel proprietario

                String sqlupdateNumberPet = "UPDATE owner SET tot_animal = ? where owner.id = ?";
                try {
                    ps = connection_db.getConnectData().prepareStatement(sqlupdateNumberPet);
                    ps.setInt(1, number_pet);
                    ps.setString(2, pet.getId_owner());
                    ps.executeUpdate();
                    //System.out.println(" Pet associato a Owner correttamente nel DB!");
                    JOptionPane.showMessageDialog(null, "Paziente associato a Proprietario correttamente nel DB");

                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }

    }

    /**
     * Metodo ereditato dall'interfaccia 'PetDAO': {@link PetDAO}  Data Access Object, permette di ricercare tutti i Pet presenti nel database.
     *
     * @return Lista di oggetti di tipo Pet {@link Pet} presenti nel database.
     */
    @Override
    public List<Pet> findAll() {
        List<Pet> listItems = new ArrayList<>();
        try {
            PreparedStatement statement = connection_db.getConnectData()
                    .prepareStatement("SELECT * FROM masterdata INNER JOIN PET ON PET.id = masterdata.id");
            ResultSet r = statement.executeQuery();
            while (r.next()) {
                listItems.add(new Pet.Builder<>()
                        .addName(r.getString("name"))
                        .addSurname(r.getString("surname"))
                        .addSex(Gender.valueOf(r.getString("sex")))
                        .addDateBirth(LocalDate.parse(r.getString("datebirth")))
                        .setId_petRace(r.getString("typepet"))
                        .setId_owner(r.getString("owner"))
                        .setParticularSign(r.getString("particularsign"))
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
     * Metodo ereditato dall'interfaccia 'PetDAO': {@link PetDAO}  Data Access Object, permette di aggiornare l'Owner presente nel database.
     *
     * @param id  del Pet {@link Pet} da modificare nel database.
     * @param pet Pet che contiene i campi aggiornati da modificare nel database.
     */
    @Override
    public void update(String id, Pet pet) {
        String sqlMasterData = "UPDATE masterdata SET name = ?, surname = ?, sex = ?, datebirth = ? where masterdata.id = ?";
        String sqlPetData = "UPDATE pet SET typepet = ?, owner = ?, particularsign = ? where pet.id = ?";
        try {
            PreparedStatement ps = connection_db.getConnectData().prepareStatement(sqlMasterData);
            ps.setString(1, pet.getName());
            ps.setString(2, pet.getSurname());
            ps.setString(3, pet.getSex().toString());
            ps.setString(4, pet.getDatebirth().toString());
            ps.setString(5, id);
            ps.executeUpdate();
            //System.out.println("Anagrafica Pet aggiornata  DB!");

            ps = connection_db.getConnectData().prepareStatement(sqlPetData);
            ps.setString(1, pet.getId_petRace());
            ps.setString(2, pet.getId_owner());
            ps.setString(3, pet.getParticularSign());
            ps.setString(4, id);
            ps.executeUpdate();

            //System.out.println("Dati personali Pet aggiornati al DB!");
            JOptionPane.showMessageDialog(null, "Modificato correttamente!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }


    }

    /**
     * Metodo ereditato dall'interfaccia 'OwnerDAO': {@link OwnerDAO}  Data Access Object, permette di cancellare il Pet presente nel database,
     * e a cascata tutti  gli Appointment associati {@link model.Appointment}, richiamando il metodo delete del ConcreteAppointmentDao {@link ConcreteAppointmentDAO}.
     *
     * @param id del Pet {@link Pet} da cancellare nel database.
     */
    @Override
    public void delete(String id) {
        //System.out.println("id da cancellare a cascata: " + id);
        PreparedStatement ps;
        try {
            //quando cancella l'animale devo aggiornare il numero di animali assocati all'owner
            //operazione da fare PRIMA della cancellazione sennò non trova il riferimento!
            String sqlSearchIdOwner = "SELECT OWNER from PET Where PET.ID = ? ";
            ps = connection_db.getConnectData().prepareStatement(sqlSearchIdOwner);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            String id_searched;
            if (rs.next()) {
                id_searched = rs.getString("owner");
                System.out.println(id_searched);
                dropPetToOwner(id_searched); //diminuisco contatore tot_animal
            } else {
                JOptionPane.showMessageDialog(null, "Ricerca Vuota");
            }

            //se cancello l'animale prima devo cancellare le prenotazioni future (data di oggi compresa) a lui associate
            ConcreteAppointmentDAO bookingDao = new ConcreteAppointmentDAO(ConnectionDBH2.getInstance());
           /* List<Appointment> futureBooking = bookingDao.findAllVisitPetAfterDateByID(id, LocalDate.now());
            futureBooking.forEach(appointment -> {
                String sqlDeleteBookingFuture = "DELETE FROM BOOKING WHERE ID_PET = ? AND DATE_VISIT = ?";
                try {
                    PreparedStatement st = connection_db.getConnectData().prepareStatement(sqlDeleteBookingFuture);
                    st.setString(1, id);
                    st.setString(2, appointment.getLocalDate().toString());
                    st.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            JOptionPane.showMessageDialog(null, "Prenotazioni future associate al paziente cancellate correttamente!");*/
            List<Appointment> allBooking = bookingDao.findAllVisitPetByID(id);
            allBooking.forEach(appointment -> {
                String sqlDeleteBooking = "DELETE FROM BOOKING WHERE ID_PET = ?";
                try {
                    PreparedStatement st = connection_db.getConnectData().prepareStatement(sqlDeleteBooking);
                    st.setString(1, id);
                    st.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            JOptionPane.showMessageDialog(null, "Prenotazioni associate al paziente cancellate correttamente!");


            //cancellazione animale
            ps = connection_db.getConnectData().prepareStatement("delete from masterdata where masterdata.id = " + "'" + id + "'");
            ps.executeUpdate();
            //System.out.println("Cancellati dati Anagrafica del Paziente!");

            ps = connection_db.getConnectData().prepareStatement("delete from PET where PET.id = " + "'" + id + "'");
            ps.executeUpdate();
            //System.out.println("Cancellati dati Paziente!");
            ps.clearParameters();

            JOptionPane.showMessageDialog(null, "Cancellato correttamente!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'PetDAO': {@link PetDAO}  Data Access Object, permette di ricercare
     * tutte le "razze" di animali presenti sul database.
     *
     * @return lista di tutte le "razze" di animali presenti sul database.
     */
    @Override
    public List<String> searchAllRace() {
        List<String> listRace = new ArrayList<>();
        String sqlSearchRace = "SELECT * FROM typepet";

        try {
            PreparedStatement statement = this.connection_db.getConnectData().prepareStatement(sqlSearchRace);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                //System.out.println(rs.getString("name"));
                listRace.add(rs.getString("name"));
            }
            return listRace;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'PetDAO': {@link PetDAO}  Data Access Object, permette di ricercare le le coppie (id,fiscalcode) associate a un Owner presenti nel database.
     *
     * @return una mappa di coppie (id, fiscalcode) degli Owner presenti nel database.
     */
    @Override
    public Map<String, String> searchAllClientByFiscalCod() {
        Map<String, String> dictionary = new HashMap<>();  //<key,value>
        String sqlSearch = """
                SELECT * FROM masterdata
                                  INNER JOIN person
                                             ON person.id = masterdata.id
                                  INNER JOIN owner
                                             ON  person.id = owner.id""";

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
     * Metodo ereditato dall'interfaccia 'PetDAO': {@link PetDAO}  Data Access Object, permette di ricercare la lista di Pet associati a un Owner.
     *
     * @param id id del Owner
     * @return la lista di Pets associati a un Owner.
     */
    @Override
    public List<Pet> searchByOwner(String id) {
        List<Pet> listPets = new ArrayList<>();
        String sqlSearchById = """
                SELECT * FROM masterdata
                       INNER JOIN pet ON pet.id = masterdata.id
                       WHERE pet.OWNER = ?""".indent(1);
        try {
            PreparedStatement statement = this.connection_db.getConnectData().prepareStatement(sqlSearchById);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                listPets.add(new Pet.Builder<>(rs.getString("typepet"),
                        rs.getString("owner"),
                        rs.getString("particularsign"))
                        .addName(rs.getString("name"))
                        .addSurname(rs.getString("surname"))
                        .addSex(Gender.valueOf(rs.getString("sex")))
                        .addDateBirth(LocalDate.parse(rs.getString("datebirth")))
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

    /**
     * Metodo ereditato dall'interfaccia 'PetDAO': {@link PetDAO}  Data Access Object, permette di ricercare
     * il Pet se presente nel database, e ritorna l'id del Pet ricercato. Se non è presente ritorna una stringa vuota id="".
     *
     * @param pet il Pet {@link Pet} da ricercare nel database.
     * @return id del Pet ricercato. Se non è presente ritorna una stringa vuota id="".
     */
    @Override
    public String search(Pet pet) {
        PreparedStatement ps;
        try {
            ps = connection_db.getConnectData().prepareStatement("SELECT * FROM masterdata" +
                    "    INNER JOIN PET" +
                    "    ON pet.id = masterdata.id" +
                    "    WHERE masterdata.name  = ?" +
                    "    AND masterdata.surname = ?" +
                    "    AND masterdata.sex = ?" +
                    "    AND masterdata.datebirth = ?");
            ps.setString(1, pet.getName());
            ps.setString(2, pet.getSurname());
            ps.setString(3, pet.getSex().toString());
            ps.setString(4, pet.getDatebirth().toString());
            ResultSet rs = ps.executeQuery();
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
     * Metodo ereditato dall'interfaccia 'PetDAO': {@link PetDAO}  Data Access Object, permette di controllare se l'oggetto Pet passato a parametro non è duplicato nel database.
     * Se non è duplicato restituisce True, altrimenti False.
     *
     * @param pet Pet {@link Pet} da ricercare nel database.
     * @return true se l'oggetto pet non è duplicato, false altrimenti.
     */
    @Override
    public boolean isNotDuplicate(Pet pet) {
        PreparedStatement ps;
        try {
            ps = connection_db.getConnectData().prepareStatement("SELECT * FROM masterdata" +
                    "    INNER JOIN PET" +
                    "    ON pet.id = masterdata.id" +
                    "    WHERE masterdata.name  = ?" +
                    "    AND masterdata.surname = ?" +
                    "    AND masterdata.sex = ?" +
                    "    AND masterdata.datebirth = ?" +
                    "    AND pet.TYPEPET = ? " +
                    "    AND pet.OWNER = ?" +
                    "    AND pet.PARTICULARSIGN = ?");
            ps.setString(1, pet.getName());
            ps.setString(2, pet.getSurname());
            ps.setString(3, pet.getSex().toString());
            ps.setString(4, pet.getDatebirth().toString());
            ps.setString(5, pet.getId_petRace());
            ps.setString(6, pet.getId_owner());
            ps.setString(7, pet.getParticularSign());

            ResultSet rs = ps.executeQuery();
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
     * Metodo ereditato dall'interfaccia 'PetDAO': {@link PetDAO}  Data Access Object, permette di ricercare l'Owner e di aumentare il numero di occorrenze di animali a suo carico.
     *
     * @param id_owner id del Owner
     */
    @Override
    public void addPetToOwner(String id_owner) {
        PreparedStatement ps;
        String sqlSearchNumAnimalOwner = "SELECT tot_animal FROM owner WHERE owner.id = ?";
        int number_pet = 0;
        try {
            ps = connection_db.getConnectData().prepareStatement(sqlSearchNumAnimalOwner);
            ps.setString(1, id_owner);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                number_pet = rs.getInt("tot_animal");
                System.out.println("numero pazienti associati adesso: " + number_pet);
            }
            number_pet += 1; //incrementa di 1 i pazienti associati al quel proprietario

            String sqlupdateNumberPet = "UPDATE owner SET tot_animal = ? where owner.id = ?";
            try {
                ps = connection_db.getConnectData().prepareStatement(sqlupdateNumberPet);
                ps.setInt(1, number_pet);
                ps.setString(2, id_owner);
                ps.executeUpdate();
                //System.out.println(" Pet associato a Owner correttamente nel DB!");
                JOptionPane.showMessageDialog(null, "Paziente associato al Proprietario correttamente nel DB");

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    /**
     * Metodo ereditato dall'interfaccia 'PetDAO': {@link PetDAO}  Data Access Object, permette di ricercare l'Owner e di decrementare il numero di occorrenze di animali a suo carico.
     *
     * @param id_owner id del Owner
     */
    @Override
    public void dropPetToOwner(String id_owner) {
        PreparedStatement ps;
        String sqlSearchNumAnimalOwner = "SELECT tot_animal FROM owner WHERE owner.id = ?";
        int number_pet = 0;
        try {
            ps = connection_db.getConnectData().prepareStatement(sqlSearchNumAnimalOwner);
            ps.setString(1, id_owner);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                number_pet = rs.getInt("tot_animal");
                System.out.println("numero pazienti associati adesso: " + number_pet);
            }
            number_pet -= 1; //diminuisce  di 1 i pazienti associati al quel proprietario
            System.out.println(number_pet);
            String sqlupdateNumberPet = "UPDATE owner SET tot_animal = ? where owner.id = ?";
            try {
                ps = connection_db.getConnectData().prepareStatement(sqlupdateNumberPet);
                ps.setInt(1, number_pet);
                ps.setString(2, id_owner);
                ps.executeUpdate();
                //System.out.println(" Pet cancellato a Owner correttamente nel DB!");
                JOptionPane.showMessageDialog(null, "Cancellata associazione tra Paziente e Proprietario correttamente nel DB");

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }
}
