package dao;

import model.Appointment;
import model.Report;

public interface ReportDAO extends Crud<Report>{

    String searchIdBookingByAppointment(Appointment appointment);
}
