package view;

import controller.ShowSpecificBookingVisitController;
import controller.ShowTablePetController;
import dao.ConcreteAppointmentDAO;
import datasource.ConnectionDBH2;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Appointment;

import javax.swing.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create an anchor pane that can store additional data.
 */
public class AnchorPaneNode extends AnchorPane {
    // Date associated with this pane
    private LocalDate date;
    private Color color;
    private ConcreteAppointmentDAO appointmentRepo;
    private List<Appointment> listAppointmentDay;
    /**
     * Create a anchor pane node. Date is not assigned in the constructor.
     * @param children children of the anchor pane
     */
    public AnchorPaneNode(Node... children) {
        super(children);
        try{
            ConnectionDBH2 connection = new ConnectionDBH2();
            this.appointmentRepo = new ConcreteAppointmentDAO(connection);
            this.listAppointmentDay = new ArrayList<>();
        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }

        // Add action handler for mouse clicked
        this.setOnMouseClicked(e -> {
            System.out.println("Data cliccata: " + date);
            this.listAppointmentDay =this.appointmentRepo.searchAppointmentsByDate(date.toString());
            this.listAppointmentDay.stream().map(Appointment::toString).forEach(System.out::println);
            //funziona fino a qui
            //#TODO:deve spuntare la lista delle prenotazioni di quel giorno

            Scene scene = this.getScene();
            BorderPane borderPane = (BorderPane) scene.lookup("#borderPane");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/showSpecificBookingVisit.fxml"));

                loader.setControllerFactory(new Callback<Class<?>, Object>() {
                    public Object call(Class<?> p) {
                        return new ShowSpecificBookingVisitController(listAppointmentDay);
                    }
                });
                borderPane.setCenter(loader.load());

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            /*
            try {
                borderPane.setCenter(FXMLLoader.load(getClass().getResource("/view/registrationClient.fxml")));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }*/
        });
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
