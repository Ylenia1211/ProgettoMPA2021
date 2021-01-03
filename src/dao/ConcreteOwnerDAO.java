package dao;

import controller.ClientController;
import datasource.ConnectionDBH2;
import model.Owner;

import javax.swing.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
           /*
            PreparedStatement statement = connection_db.dbConnection().prepareStatement("SELECT * FROM masterdata" +
                    "     INNER JOIN person\n" +
                    "    ON person.id = masterdata.id" +
                    "    INNER JOIN owner  " +
                    "    ON  person.id = owner.id;");
            ResultSet rs = statement.executeQuery();
            List<String> db_client =  new ArrayList<String>();
            while (rs.next()) {
                String id = rs.getString("id");
                String name =  rs.getString("name");
                System.out.println(id + ' ' + name);
                db_client.add(name);
            }
            JOptionPane.showMessageDialog(null, "Owner Registrati:\n" +  db_client);

            */
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }


    }

    @Override
    public ResultSet findAll() {
        PreparedStatement ps = null;
        try {
            PreparedStatement statement = connection_db.dbConnection().prepareStatement("select * from owner");
            ResultSet rs = statement.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return null;
        }
    }

    @Override
    public void update(String id) {

    }

    @Override
    public void delete(String id) {

    }
}
