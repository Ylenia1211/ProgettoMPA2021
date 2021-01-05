package dao;

import java.sql.ResultSet;

public interface Crud<T> {
    void add(T item);    //create
    ResultSet findAll(); //read
    void update(String id, T item);
    void delete(String id);
}