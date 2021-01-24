package dao;

import model.Doctor;
import model.Secretariat;
import model.User;

public interface SecretariatDAO extends Crud<Secretariat>{
    String search(Secretariat data);
    Secretariat searchByUsernameAndPassword(User user);
    boolean isNotDuplicate(Secretariat data); //fa controlli piu stretti rispetto al search
}
