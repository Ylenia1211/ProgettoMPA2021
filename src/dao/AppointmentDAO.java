package dao;

import model.Appointment;
import model.Owner;
import model.Pet;

import java.util.List;
import java.util.Map;

public interface AppointmentDAO extends Crud<Appointment> {
    Map<String, String> searchAllClientByFiscalCod();
    Map<String, String> searchPetsByOwner(String id);
    Map<String, String> searchAllDoctorByFiscalCod();
    String searchSpecializationByDoctor(String idDoctorSearched);
    List<Appointment> searchAppointmentsByDate(String date);
    Integer countAppointmentsByDate(String date);
    String search(Appointment appointment);
    String searchEmailOwnerbyIdAppointment(String id);

    Pet searchPetById(String id);

    Owner searchOwnerById(String id);
}
