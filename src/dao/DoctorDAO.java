package dao;


import model.Doctor;
import model.Owner;
import model.User;

import java.util.List;

public interface DoctorDAO extends Crud<Doctor>{

    List<String> searchAllSpecialization();

    String search(Doctor data);

    Doctor searchByUsernameAndPassword(User user);
}
