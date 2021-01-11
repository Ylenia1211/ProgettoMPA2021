package dao;


import model.Doctor;
import model.Owner;

import java.util.List;

public interface DoctorDAO extends Crud<Doctor>{

    List<String> searchAllSpecialization();

    String search(Doctor data);
}
