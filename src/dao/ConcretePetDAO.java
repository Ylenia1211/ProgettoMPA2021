package dao;

import datasource.ConnectionDBH2;
import model.Pet;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            ps = connection_db.dbConnection().prepareStatement("insert into masterdata(id, name, surname,sex, datebirth) values(?,?,?,?,?)");
            ps.setString(1, pet.getId());
            ps.setString(2, pet.getName());
            ps.setString(3, pet.getSurname());
            ps.setString(4, pet.getSex());
            //LocalDate ld = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(owner.getDatebirth()));
            ps.setString(5, pet.getDatebirth().toString());
            ps.executeUpdate();
            System.out.println("Anagrafica Pet aggiunta al DB!");


            ps = null;
            ps = connection_db.dbConnection().prepareStatement("insert into pet(id, typepet, owner, particularsign) values(?,?,?,?)");
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
            int number_pet=0;
            try {
                ps = connection_db.dbConnection().prepareStatement(sqlSearchNumAnimalOwner);
                ps.setString(1, pet.getId_owner());
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                   number_pet = rs.getInt("tot_animal");
                   System.out.println("numero pazienti associati adesso: " + number_pet);
                }
                number_pet +=1; //incrementa di 1 i pazienti associati al quel proprietario

                String sqlupdateNumberPet = "UPDATE owner SET tot_animal = ? where owner.id = ?";
                try {
                    ps = connection_db.dbConnection().prepareStatement(sqlupdateNumberPet);
                    ps.setInt(1, number_pet);
                    ps.setString(2, pet.getId_owner());
                    ps.executeUpdate();
                    System.out.println(" Pet associato a Owner correttamente nel DB!");
                    JOptionPane.showMessageDialog(null, "Pet associato a Owner correttamente nel DB");

                }catch (SQLException e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
                }

            }catch (SQLException e){
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
    public void update(String id, Pet item) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public List<String> searchAllRace() {
        List<String> listRace = new ArrayList<String>();
        PreparedStatement ps = null;
        //String sqlSearchRace = "";
        String sqlSearchRace = "SELECT * FROM typepet";

        try {
            PreparedStatement statement = this.connection_db.dbConnection().prepareStatement(sqlSearchRace);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                //System.out.println(rs.getString("name"));
                listRace.add(rs.getString("name"));
            }
            return listRace;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    //#Todo: da modificare ricerca per cognome e cod.fiscale
    @Override
    public Map<String, String> searchAllClientBySurnameAndFiscalCod() {
        //List<String> list = new ArrayList<String>();
        HashMap<String,Map<String, String>> linkedMap = new HashMap<>();
        Map<String, String> dictionary = new HashMap<>();  //<key,value>  both key and value are Strings
        PreparedStatement ps = null;
        String sqlSearch = "SELECT * FROM masterdata\n" +
                "                  INNER JOIN person\n" +
                "                             ON person.id = masterdata.id\n" +
                "                  INNER JOIN owner\n" +
                "                             ON  person.id = owner.id";

        try {
            PreparedStatement statement = this.connection_db.dbConnection().prepareStatement(sqlSearch);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                //System.out.println(rs.getString("surname"));
                //list.add(rs.getString("surname"));
                dictionary.put( rs.getString("id"), rs.getString("fiscalcode"));
                //linkedMap.put(rs.getString("id") , dictionary);
            }
            //return list;
            dictionary.entrySet().forEach(System.out::println);

            return dictionary;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
