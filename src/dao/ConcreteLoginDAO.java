package dao;

import datasource.ConnectionDBH2;
import model.User;


public class ConcreteLoginDAO{
    private final ConnectionDBH2 connection_db;
    public  ConcreteLoginDAO(ConnectionDBH2 connection_db) {
        this.connection_db = connection_db;
    }

   /* //#TOdo: implementare
    * Metodo che mi ricerca in base all'utente loggato se la password e l'username sono esistenti e corretti
    */
    public boolean searchUser(User userLogged) {
        return true;
    }
}
