package dao;

import model.Client;

import java.sql.ResultSet;
import java.util.List;

public interface ClientDAO {
    void add(Client item);
    //void update(Client item);
    //void delete(int id);
    //Client read(int id);

    ResultSet findAll();
}
