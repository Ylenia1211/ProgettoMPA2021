package dao;

import datasource.ConnectionDBH2;
import model.Secretariat;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ConcreteSecretariatDAO implements SecretariatDAO {
    private final ConnectionDBH2 connection_db;
    public ConcreteSecretariatDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

    @Override
    public void add(Secretariat secretariat) {
        PreparedStatement ps;
        try {
            ps =connection_db.getConnectData().prepareStatement("insert into masterdata(id, name, surname,sex, datebirth) values(?,?,?,?,?)");
            ps.setString(1, secretariat.getId());
            ps.setString(2, secretariat.getName());
            ps.setString(3, secretariat.getSurname());
            ps.setString(4, secretariat.getSex().toString());
            ps.setString(5, secretariat.getDatebirth().toString());
            ps.executeUpdate();
            System.out.println("Anagrafica secretariat aggiunta al DB!");

            ps =connection_db.getConnectData().prepareStatement("insert into person(id, address, city, telephone, email, FISCALCODE) values(?,?,?,?,?,?)");
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

    @Override
    public List<Secretariat> findAll() {
        return null;
    }

    @Override
    public void update(String id, Secretariat item) {

    }

    @Override
    public void delete(String id) {

    }
}
