package dao;

import model.Appointment;
import model.Doctor;
import model.Owner;
import model.Pet;

import java.time.LocalDate;
import java.time.LocalTime;
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
    boolean searchIfExistAppointmentInReport(String id);
    List<Appointment> searchVisitbyDoctorAndDate(String idDoctorSearched, String date); //mi serve per l'agenda
    Doctor searchDoctorById(String id);
    Integer countAppointmentsByDateAndDoctor(String date, String id_doctor); //mi serve
    List<Appointment> searchVisitBeforeDate(LocalDate date, LocalTime now); // mi serve per prendere tutte le visite precedenti a una data (vista condivisa dottori)
    List<Appointment> searchVisitAfterDate(LocalDate date,  LocalTime now); // mi serve per prendere tutte le visite successive o uguali a una data
    List<Appointment> searchVisitByDoctorBeforeDate(LocalDate date, LocalTime now, String id_doctor); // mi serve per prendere tutte le visite precedenti a una data (vista condivisa dottori)
    List<Appointment> searchVisitbyDoctorOrPetAndDate(String idDoctorSearched,String id_pet, String date); //mi serve per il controllo delle sovrapposizioni
   List<Appointment> findAllVisitPet(String name, String surname);
    List<Appointment> findAllVisitPetBeforeDate(String name, String surname, LocalDate date);
    List<Appointment> findAllVisitPetAfterDate(String name, String surname, LocalDate date);

    List<Appointment> findAllVisitPetByID(String id);
    List<Appointment> findAllVisitPetAfterDateByID(String id, LocalDate date);

}

