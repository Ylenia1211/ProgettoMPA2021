package dao;

import model.Appointment;
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
}
