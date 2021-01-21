package dao;

import model.Doctor;
import model.Secretariat;
import model.User;

public interface SecretariatDAO extends Crud<Secretariat>{
    String search(Secretariat data);

    Secretariat searchByUsernameAndPassword(User user);
}
