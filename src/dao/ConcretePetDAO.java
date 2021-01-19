package dao;

import datasource.ConnectionDBH2;
import model.Gender;
import model.Pet;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ConcretePetDAO implements PetDAO {
    private ConnectionDBH2 connection_db;

    public ConcretePetDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

    @Override
    public void add(Pet pet) {
        PreparedStatement ps = null;
        try {
            ps = connection_db.getConnectData().prepareStatement("insert into masterdata(id, name, surname,sex, datebirth) values(?,?,?,?,?)");
            ps.setString(1, pet.getId());
            ps.setString(2, pet.getName());
            ps.setString(3, pet.getSurname());
            ps.setString(4, pet.getSex().toString());
            //LocalDate ld = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(owner.getDatebirth()));
            ps.setString(5, pet.getDatebirth().toString());
            ps.executeUpdate();
            System.out.println("Anagrafica Pet aggiunta al DB!");


            ps = null;
            ps = connection_db.getConnectData().prepareStatement("insert into pet(id, typepet, owner, particularsign) values(?,?,?,?)");
            ps.setString(1, pet.getId());
            ps.setString(2, pet.getId_petRace());
            ps.setString(3, pet.getId_owner());
            ps.setString(4, pet.getParticularSign());

            ps.executeUpdate();
            System.out.println("Dati personali Pet aggiunti al DB!");
            JOptionPane.showMessageDialog(null, "Pet aggiunto correttamente!");


            /**
             * Dobbiamo cercare il numero di pazienti (pet) associati al cliente (owner) e incrementare il numero di pazienti associati
             **/
            String sqlSearchNumAnimalOwner = "SELECT tot_animal FROM owner WHERE owner.id = ?";
            ps = null;
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
                    System.out.println(" Pet associato a Owner correttamente nel DB!");
                    JOptionPane.showMessageDialog(null, "Pet associato a Owner correttamente nel DB");

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

    @Override
    public ResultSet findAll() {
        return null;
    }

    @Override
    public void update(String id, Pet pet) {
        String sqlMasterData = "UPDATE masterdata SET name = ?, surname = ?, sex = ?, datebirth = ? where masterdata.id = ?";
        String sqlPetData = "UPDATE pet SET typepet = ?, owner = ?, particularsign = ? where pet.id = ?";
        PreparedStatement ps = null;
        try {
            ps = connection_db.getConnectData().prepareStatement(sqlMasterData);
            ps.setString(1, pet.getName());
            ps.setString(2, pet.getSurname());
            ps.setString(3, pet.getSex().toString());
            ps.setString(4, pet.getDatebirth().toString());
            ps.setString(5, id);
            ps.executeUpdate();
            System.out.println("Anagrafica Pet aggiornata  DB!");

            ps = null;
            ps = connection_db.getConnectData().prepareStatement(sqlPetData);
            ps.setString(1, pet.getId_petRace());
            ps.setString(2, pet.getId_owner());
            ps.setString(3, pet.getParticularSign());
            ps.setString(4, id);
            ps.executeUpdate();

            System.out.println("Dati personali Pet aggiornati al DB!");
            JOptionPane.showMessageDialog(null, "Modificato correttamente!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }


    }

    @Override
    public void delete(String id) {
        System.out.println("id da cancellare a cascata: " + id);
        PreparedStatement ps = null;
        try {

            //quando cancella l'animale devo aggiornare il numero di animali assocati all'owner
            //operazione da fare PRIMA della cancellazione sennò non trova il riferimento!
            String sqlSearchIdOwner = "SELECT OWNER from PET Where PET.ID = ? ";
            ps = connection_db.getConnectData().prepareStatement(sqlSearchIdOwner);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            String id_searched = "";
            if (rs.next()) {
                id_searched = rs.getString("owner");
                System.out.println(id_searched);
                dropPetToOwner(id_searched); //diminuisco contatore tot_animal
            } else {
                JOptionPane.showMessageDialog(null, "Ricerca Vuota");
            }

            //cancellazione animale
            ps = connection_db.getConnectData().prepareStatement("delete from masterdata where masterdata.id = " + "\'" + id + "\'");
            ps.executeUpdate();
            System.out.println("Cancellati dati Anagrafica del Pet!");

            ps = connection_db.getConnectData().prepareStatement("delete from PET where PET.id = " + "\'" + id + "\'");
            ps.executeUpdate();
            System.out.println("Cancellati dati Pet!");
            ps.clearParameters();

            JOptionPane.showMessageDialog(null, "Cancellato correttamente!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    @Override
    public List<String> searchAllRace() {
        List<String> listRace = new ArrayList<String>();
        PreparedStatement ps = null;
        //String sqlSearchRace = "";
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


    @Override
    public Map<String, String> searchAllClientByFiscalCod() {
        //List<String> list = new ArrayList<String>();
        HashMap<String, Map<String, String>> linkedMap = new HashMap<>();
        Map<String, String> dictionary = new HashMap<>();  //<key,value>  both key and value are Strings
        PreparedStatement ps = null;
        String sqlSearch = "SELECT * FROM masterdata\n" +
                "                  INNER JOIN person\n" +
                "                             ON person.id = masterdata.id\n" +
                "                  INNER JOIN owner\n" +
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

    @Override
    public List<Pet> searchByOwner(String id) {
        List<Pet> listPets = new ArrayList<>();
        String sqlSearchById = " SELECT * FROM masterdata\n" +
                "        INNER JOIN pet ON pet.id = masterdata.id\n" +
                "        WHERE pet.OWNER = ?";
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

    @Override
    public String search(Pet pet) {
        PreparedStatement ps = null;
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

    @Override
    public void addPetToOwner(String id_owner) {
        PreparedStatement ps = null;
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
                System.out.println(" Pet associato a Owner correttamente nel DB!");
                JOptionPane.showMessageDialog(null, "Pet associato a Owner correttamente nel DB");

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    @Override
    public void dropPetToOwner(String id_owner) {
        PreparedStatement ps = null;
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
                System.out.println(" Pet cancellato a Owner correttamente nel DB!");
                JOptionPane.showMessageDialog(null, "Pet cancellato a Owner correttamente nel DB");

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
