package dao;


import model.Doctor;

public interface DoctorDAO extends Crud<Doctor>{

    void searchAllSpecialization();
}
