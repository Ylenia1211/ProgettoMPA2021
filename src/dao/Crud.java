package dao;

import java.sql.ResultSet;
import java.util.List;

public interface Crud<T> {
    void add(T item);    //create
    List<T> findAll(); //read
    void update(String id, T item);
    void delete(String id);
}
