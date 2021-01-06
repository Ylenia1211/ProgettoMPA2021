package dao;

import datasource.ConnectionDBH2;
import model.Pet;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConcretePetDAO implements PetDAO {
    private ConnectionDBH2 connection_db;

    public ConcretePetDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

    @Override
    public void add(Pet pet) {
        PreparedStatement ps = null;
        try {
            //ps = connection_db.dbConnection().prepareStatement("insert into masterdata(id, name, surname,sex, datebirth) values(?,?,?,?,?)");
            ps.setString(1, pet.getId());
            ps.setString(2, pet.getName());
            ps.setString(3, pet.getSurname());
            ps.setString(4, pet.getSex());
            //LocalDate ld = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(owner.getDatebirth()));
            ps.setString(5, pet.getDatebirth().toString());
            ps.executeUpdate();
            System.out.println("Anagrafica Pet aggiunta al DB!");


            ps = null;
            //ps = connection_db.dbConnection().prepareStatement("insert into pet(id, typepet, owner, particularsign) values(?,?,?,?)");
            ps.setString(1, pet.getId());
            ps.setString(2, pet.getId_petRace());
            ps.setString(3, pet.getId_owner());
            ps.setString(4, pet.getParticularSign());

            ps.executeUpdate();
            System.out.println("Dati personali Pet aggiunti al DB!");
            JOptionPane.showMessageDialog(null, "Pet aggiunto correttamente!");
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
        String sqlSearchRace = "";
        //String sqlSearchRace = "SELECT * FROM typepet";

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

    @Override
    public List<String> searchAllSurnameClient() {
        List<String> list = new ArrayList<String>();
        PreparedStatement ps = null;

        //String sqlSearch = "SELECT * FROM masterdata";
        String sqlSearch = "";
        try {
            PreparedStatement statement = this.connection_db.dbConnection().prepareStatement(sqlSearch);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                //System.out.println(rs.getString("surname"));
                list.add(rs.getString("surname"));
            }
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
