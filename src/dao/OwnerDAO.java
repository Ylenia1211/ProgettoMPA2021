package dao;


import model.Owner;

import java.sql.ResultSet;
import java.util.List;

public interface OwnerDAO extends Crud<Owner>{
    // QUA inserire solo ricerche specifiche per l'Owner

    String search(Owner client); //ritorna l'id dell'owner

    boolean isNotDuplicate(Owner p); //fa controlli piu stretti rispetto al search

}
