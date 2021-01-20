package dao;

import controller.ClientController;
import datasource.ConnectionDBH2;
import model.Appointment;
import model.Gender;
import model.Owner;

import javax.swing.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ConcreteOwnerDAO implements OwnerDAO{
    private final ConnectionDBH2 connection_db;
    public  ConcreteOwnerDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

    @Override
    public void add(Owner owner) {

        PreparedStatement ps = null;
        try {
            ps = connection_db.getConnectData().prepareStatement("insert into masterdata(id, name, surname,sex, datebirth) values(?,?,?,?,?)");
            ps.setString(1, owner.getId());
            ps.setString(2, owner.getName());
            ps.setString(3, owner.getSurname());
            ps.setString(4, owner.getSex().toString());
            //LocalDate ld = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(owner.getDatebirth()));
            ps.setString(5, owner.getDatebirth().toString());
            ps.executeUpdate();
            System.out.println("Anagrafica Owner aggiunta al DB!");


            ps = null;
            ps = connection_db.getConnectData().prepareStatement("insert into person(id, address, city, telephone, email, fiscalcode) values(?,?,?,?,?,?)");
            ps.setString(1, owner.getId());
            ps.setString(2, owner.getAddress());
            ps.setString(3, owner.getCity());
            ps.setString(4, owner.getTelephone());
            ps.setString(5, owner.getEmail());
            ps.setString(6, owner.getFiscalCode());
            ps.executeUpdate();
            System.out.println("Dati civici Owner aggiunti al DB!");

            ps = null;
            ps = connection_db.getConnectData().prepareStatement("insert into owner(id, tot_animal) values(?,?)");
            ps.setString(1, owner.getId());
            ps.setInt(2, owner.getTot_animal());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Owner aggiunto correttamente!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }


    }

    @Override
    public List<Owner> findAll() {
       List<Owner> listItems = new ArrayList<>();
        try {
            PreparedStatement statement = connection_db.getConnectData()
                    .prepareStatement(" SELECT * FROM masterdata INNER JOIN person ON person.id = masterdata.id INNER JOIN owner ON  person.id = owner.id ");
            ResultSet r = statement.executeQuery();
            while(r.next()) {
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

    @Override
    public void update(String id, Owner client) {
        String sqlMasterData = "UPDATE masterdata SET name = ?, surname = ?, sex = ?, datebirth = ? where masterdata.id = ?";
        String sqlPersonData = "UPDATE person SET address = ?, city = ?, telephone = ?, email = ?, fiscalcode = ? where person.id = ?";

        PreparedStatement ps = null;
        try {
            ps = connection_db.getConnectData().prepareStatement(sqlMasterData);
            ps.setString(1, client.getName());
            ps.setString(2, client.getSurname());
            ps.setString(3, client.getSex().toString());
            ps.setString(4, client.getDatebirth().toString());
            ps.setString(5, id);
            ps.executeUpdate();

            System.out.println("Aggiornati dati Anagrafica del Owner!");
            ps = null;
            ps = connection_db.getConnectData().prepareStatement(sqlPersonData);
            ps.setString(1, client.getAddress());
            ps.setString(2, client.getCity());
            ps.setString(3, client.getTelephone());
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getFiscalCode());
            ps.setString(6, id);
            ps.executeUpdate();
            System.out.println("Aggiornati dati persona del Owner!");
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
            ps = connection_db.getConnectData().prepareStatement("delete from masterdata where masterdata.id = "+"\'"+ id +"\'" );
            ps.executeUpdate();
            System.out.println("Cancellati dati Anagrafica del Owner!");

            ps = null;
            ps = connection_db.getConnectData().prepareStatement("delete from person where person.id = "+"\'"+ id +"\'" );

            ps.executeUpdate();
            System.out.println("Cancellati dati civici del Owner!");

            ps = null;
            ps = connection_db.getConnectData().prepareStatement("delete from owner where owner.id = "+"\'"+ id +"\'" );

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
            PreparedStatement statement = connection_db.getConnectData().prepareStatement("SELECT * FROM masterdata" +
                    "    INNER JOIN person" +
                    "    ON person.id = masterdata.id" +
                    "    INNER JOIN owner  " +
                    "    ON  person.id = owner.id WHERE masterdata.name = ? AND masterdata.surname = ? " +
                    "    AND masterdata.sex = ?"+
                    "    AND masterdata.datebirth = ? AND PERSON.FISCALCODE = ?");
            statement.setString(1, client.getName());
            statement.setString(2, client.getSurname());
            statement.setString(3, client.getSex().toString());
            statement.setString(4, client.getDatebirth().toString());
            statement.setString(5, client.getFiscalCode());
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
