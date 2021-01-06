package dao;


import model.Doctor;

import java.util.List;

public interface DoctorDAO extends Crud<Doctor>{

    List<String> searchAllSpecialization();
}
