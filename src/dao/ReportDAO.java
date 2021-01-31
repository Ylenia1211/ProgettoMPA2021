package dao;

import model.Appointment;
import model.Report;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Interfaccia Data Access Object per il tipo di oggetto {@link Report} estende l'interfaccia generica {@link Crud}.
 * Dichiara i metodi specifici per un oggetto di tipo Report {@link Report}.
 */
public interface ReportDAO extends Crud<Report> {
    String searchIdBookingByAppointment(Appointment appointment);

    Report searchByIdBooking(String idBooking);
}
