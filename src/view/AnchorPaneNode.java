package view;
import controller.ShowSpecificBookingVisitController;
import dao.ConcreteAppointmentDAO;
import dao.ConcreteDoctorDAO;
import datasource.ConnectionDBH2;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.Appointment;
import util.SessionUser;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata che estende un 'AnchorPane':{@link AnchorPane} in modo da riempirlo con dati aggiuntivi.
 */
public class AnchorPaneNode extends AnchorPane {
    private LocalDate date;
    private final ConcreteAppointmentDAO appointmentRepo;
    private List<Appointment> listAppointmentDay;
    private final ConcreteDoctorDAO doctorRepo;

    /**
     * Crea un nodo del Anchor Pane.
     * @param children figli dell'AnchorPane
     */
    public AnchorPaneNode(Node... children) {
        super(children);

        this.appointmentRepo = new ConcreteAppointmentDAO(ConnectionDBH2.getInstance());
        this.listAppointmentDay = new ArrayList<>();
        this.doctorRepo = new ConcreteDoctorDAO(ConnectionDBH2.getInstance());

        this.setOnMouseClicked(e -> {
            System.out.println("Data cliccata: " + date);

            //deve spuntare la lista delle prenotazioni di quel giorno per l'utente loggato OK
            String id_doctor = this.doctorRepo.search(SessionUser.getDoctor());
            //System.out.println("id_dottore loggato: " + id_doctor);
            this.listAppointmentDay = this.appointmentRepo.searchVisitbyDoctorAndDate(id_doctor,date.toString());

            //this.listAppointmentDay =this.appointmentRepo.searchAppointmentsByDate(date.toString()); //questo è generale NON Cancellare
            //this.listAppointmentDay.stream().map(Appointment::toString).forEach(System.out::println);

            Scene scene = this.getScene();
            BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/showSpecificBookingVisit.fxml"));

                loader.setControllerFactory(p -> new ShowSpecificBookingVisitController(listAppointmentDay));
                borderPane.setCenter(loader.load());

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        });
    }
    /**
     * Metodo che restituisce la data di una cella dell'Agenda
     *
     * @return data corrispondente
     */
    public LocalDate getDate() {
        return date;
    }
    /**
     * Metodo per settare la data di una cella dell'Agenda utilizzato in {@link FullCalendarView}
     *
     * @param date corrispondente
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

}
