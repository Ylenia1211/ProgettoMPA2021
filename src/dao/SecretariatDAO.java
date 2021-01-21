package dao;

import model.Doctor;
import model.Secretariat;

public interface SecretariatDAO extends Crud<Secretariat>{
    String search(Secretariat data);
}
