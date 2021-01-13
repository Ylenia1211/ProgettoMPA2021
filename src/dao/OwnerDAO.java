package dao;


import model.Owner;

import java.sql.ResultSet;
import java.util.List;

public interface OwnerDAO extends Crud<Owner>{

    String search(Owner client);
    // QUA inserire solo ricerche specifiche per l'Owner

}
