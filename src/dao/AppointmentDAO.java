package dao;

import model.Appointment;
import model.Pet;

import java.util.List;
import java.util.Map;

public interface AppointmentDAO extends Crud<Appointment> {
    Map<String, String> searchAllClientByFiscalCod();
    //Map<String, String> searchAllDoctorByFiscalCod();
    List<Pet> searchPetsByOwner(String id);
}
