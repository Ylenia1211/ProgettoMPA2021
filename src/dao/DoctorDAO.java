package dao;


import model.Doctor;
import model.Owner;
import model.User;

import javax.print.Doc;
import java.util.List;

public interface DoctorDAO extends Crud<Doctor>{

    List<String> searchAllSpecialization();
    String search(Doctor data);
    Doctor searchByUsernameAndPassword(User user);
    Doctor searchById(String id);
    boolean isNotDuplicate(Doctor doctor); //fa controlli piu stretti rispetto al search

    void updatePassword(String id, String pwd);
}
