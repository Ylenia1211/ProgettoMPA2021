package dao;

import datasource.ConnectionDBH2;
import model.Client;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConcreteClientDAO implements ClientDAO{
    private ConnectionDBH2 connection_db;

    public ConcreteClientDAO(ConnectionDBH2 con) {
        this.connection_db = con;
    }

    @Override
    public void add(Client client) {

        PreparedStatement ps = null;
        try {
            ps = connection_db.dbConnection().prepareStatement("insert into client(name, surname,address, city, telephone, email) values(?,?,?,?,?,?)");
            ps.setString(1, client.getFirstName());
            ps.setString(2, client.getLastName());
            ps.setString(3, client.getAddress());
            ps.setString(4, client.getCity());
            ps.setString(5, client.getTelephone());
            ps.setString(6, client.getEmail());
            ps.executeUpdate();
            System.out.println("Cliente aggiunto al DB!");
            JOptionPane.showMessageDialog(null, "Cliente aggiunto correttamente!");
            PreparedStatement statement = connection_db.dbConnection().prepareStatement("select * from client");
            ResultSet rs = statement.executeQuery();
            List<String> db_client =  new ArrayList<String>();
            while (rs.next()) {
                String id = rs.getString("id");
                String name =  rs.getString("name");
                System.out.println(id + ' ' + name);
                db_client.add(name);
            }
            JOptionPane.showMessageDialog(null, "Clienti Registrati:\n" +  db_client);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    @Override
    public ResultSet findAll() {
        PreparedStatement ps = null;
        try {
            PreparedStatement statement = connection_db.dbConnection().prepareStatement("select * from client");
            ResultSet rs = statement.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return null;
        }
    }
}
