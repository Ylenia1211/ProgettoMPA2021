package dao;

import java.util.List;
/**
 *
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 *
 * Interfaccia generica per  Data Access Object che può essere usata per qualsiasi tipo di oggetto.
 * Dichiara i metodi Crud (Create, Read, Update, Delete) per ogni tipo di oggetto generico T .
 *
 */
public interface Crud<T> {
    void add(T item);    //create
    List<T> findAll(); //read
    void update(String id, T item);
    void delete(String id);
}
