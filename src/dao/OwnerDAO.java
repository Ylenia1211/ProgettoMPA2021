package dao;


import model.Owner;

import java.sql.ResultSet;
import java.util.List;

public interface OwnerDAO {
    void add(Owner item);
    //void update(Client item);
    //void delete(int id);
    //Client read(int id);

    ResultSet findAll();
}
