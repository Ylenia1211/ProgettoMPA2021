package dao;

import controller.ClientController;
import datasource.ConnectionDBH2;
import model.Owner;

import javax.swing.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ConcreteOwnerDAO implements OwnerDAO{
    private ConnectionDBH2 connection_db;

    public ConcreteOwnerDAO(ConnectionDBH2 con) {
        this.connection_db = con;
    }

    @Override
    public void add(Owner owner) {

        PreparedStatement ps = null;
        try {
            ps = connection_db.dbConnection().prepareStatement("insert into masterdata(id, name, surname,sex, datebirth) values(?,?,?,?,?)");
            ps.setString(1, owner.getId());
            ps.setString(2, owner.getName());
            ps.setString(3, owner.getSurname());
            ps.setString(4, owner.getSex());
            //LocalDate ld = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(owner.getDatebirth()));
            ps.setString(5, owner.getDatebirth().toString());
            ps.executeUpdate();
            System.out.println("Anagrafica Owner aggiunta al DB!");


            ps = null;
            ps = connection_db.dbConnection().prepareStatement("insert into person(id, address, city, telephone, email) values(?,?,?,?,?)");
            ps.setString(1, owner.getId());
            ps.setString(2, owner.getAddress());
            ps.setString(3, owner.getCity());
            ps.setString(4, owner.getTelephone());
            ps.setString(5, owner.getEmail());
            ps.executeUpdate();
            System.out.println("Dati civici Owner aggiunti al DB!");

            ps = null;
            ps = connection_db.dbConnection().prepareStatement("insert into owner(id, tot_visit) values(?,?)");
            ps.setString(1, owner.getId());
            ps.setInt(2, owner.getTot_visit());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Owner aggiunto correttamente!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }


    }

    @Override
    public ResultSet findAll() {
        PreparedStatement ps = null;
        try {
            PreparedStatement statement = connection_db.dbConnection().prepareStatement("SELECT * FROM masterdata" +
                    "    INNER JOIN person" +
                    "    ON person.id = masterdata.id" +
                    "    INNER JOIN owner  " +
                    "    ON  person.id = owner.id");
            ResultSet rs = statement.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return null;
        }
    }

    @Override
    public void update(String id, Owner client) {
        String sqlMasterData = "UPDATE masterdata SET name = ?, surname = ?, sex = ?, datebirth = ? where masterdata.id = ?";

        PreparedStatement ps = null;
        try {
            ps = connection_db.dbConnection().prepareStatement(sqlMasterData);
            ps.setString(1, client.getName());
            ps.setString(2, client.getSurname());
            ps.setString(3, client.getSex());
            ps.setString(4, client.getDatebirth().toString());
            ps.setString(5, id);
            ps.executeUpdate();

            System.out.println("Aggiornati dati Anagrafica del Owner!");

            //#TODO: Aggiornare le altre tabelle


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
            ps = connection_db.dbConnection().prepareStatement("delete from masterdata where masterdata.id = "+"\'"+ id +"\'" );
            ps.executeUpdate();
            System.out.println("Cancellati dati Anagrafica del Owner!");

            ps = null;
            ps = connection_db.dbConnection().prepareStatement("delete from person where person.id = "+"\'"+ id +"\'" );

            ps.executeUpdate();
            System.out.println("Cancellati dati civici del Owner!");

            ps = null;
            ps = connection_db.dbConnection().prepareStatement("delete from owner where owner.id = "+"\'"+ id +"\'" );

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cancellato correttamente!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    @Override
    public String search(Owner client) {
        PreparedStatement ps = null;
        try{
            PreparedStatement statement = connection_db.dbConnection().prepareStatement("SELECT * FROM masterdata" +
                    "    INNER JOIN person" +
                    "    ON person.id = masterdata.id" +
                    "    INNER JOIN owner  " +
                    "    ON  person.id = owner.id WHERE masterdata.name =" +"\'"+ client.getName() +"\'"+
                    "    AND masterdata.surname =" +"\'"+ client.getSurname() +"\'"+
                    "    AND masterdata.sex =" +"\'"+ client.getSex() +"\'"+
                    "    AND masterdata.datebirth =" +"\'"+ client.getDatebirth() +"\'");
            //System.out.println(client.getName());
            ResultSet rs = statement.executeQuery();
            String id_searched ="";
            if(rs.next()){
                id_searched  = rs.getString("id");
                return id_searched;
            }else{
                JOptionPane.showMessageDialog(null, "Ricerca Vuota");
                return id_searched;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return null;
        }
    }
}
