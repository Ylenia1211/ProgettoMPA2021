package view;

import dao.ConcreteAppointmentDAO;
import dao.ConcreteDoctorDAO;
import datasource.ConnectionDBH2;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import util.SessionUser;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

import static javafx.scene.layout.AnchorPane.*;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per la generazione di un agenda, viene richiamata dal Controller {@link controller.CalendarController}.
 */
public class FullCalendarView {

    private final ArrayList<AnchorPaneNode> allCalendarDays = new ArrayList<>(35);
    private final VBox view;
    private final Label calendarTitle;
    private YearMonth currentYearMonth;
    private final ConcreteAppointmentDAO appointmentRepo;
    private final ConcreteDoctorDAO doctorRepo;

    /**
     * Metodo Costruttore crea la view dell'agenda a partire dal mese e anno settati
     *
     * @param yearMonth mese e anno settati in {@link controller.CalendarController}
     */
    public FullCalendarView(YearMonth yearMonth) {

        this.appointmentRepo = new ConcreteAppointmentDAO(ConnectionDBH2.getInstance());
        this.doctorRepo = new ConcreteDoctorDAO(ConnectionDBH2.getInstance());
        currentYearMonth = yearMonth;

        // crea la griglia per il calendario
        GridPane calendar = new GridPane();
        calendar.setPrefSize(800, 600); //dimensione agenda
        calendar.setGridLinesVisible(true);
        // crea le celle necessarie per le settimane e i giorni dell'agenda
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                AnchorPaneNode ap = new AnchorPaneNode();
                ap.setPrefSize(200, 200);
                calendar.add(ap, j, i);
                allCalendarDays.add(ap);
            }
        }
        //giorni della settimana
        Label[] dayNames = new Label[]{new Label("Lunedi"), new Label("Martedi"),
                new Label("Mercoledi"), new Label("Giovedi"), new Label("Venerdi"),
                new Label("Sabato"), new Label("Domenica")};
        GridPane dayLabels = new GridPane();
        dayLabels.setPrefWidth(600);
        int col = 0;
        for (Label txt : dayNames) {
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(200, 10);
            txt.setFont(Font.font("Calibri", 20));
            txt.setStyle("-fx-text-fill: #163754");
            setBottomAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            dayLabels.add(ap, col++, 0);
        }
        // Creare calendarTitle e i pulsanti per cambiare il mese corrente
        calendarTitle = new Label();
        calendarTitle.setFont(Font.font("Calibri", 15));
        Button previousMonth = new Button("<<");
        previousMonth.setStyle("-fx-background-color: white; -fx-background-radius: 30px, 30px, 30px, 30px;");
        previousMonth.setEffect(new DropShadow(+2d, 0d, +2d, Color.LIGHTBLUE));
        previousMonth.setOnAction(e -> previousMonth());
        Button nextMonth = new Button(">>");
        nextMonth.setStyle("-fx-background-color: white; -fx-background-radius: 30px, 30px, 30px, 30px;");
        nextMonth.setEffect(new DropShadow(+2d, 0d, +2d, Color.LIGHTBLUE));
        nextMonth.setOnAction(e -> nextMonth());
        HBox titleBar = new HBox(previousMonth, calendarTitle, nextMonth);
        titleBar.setAlignment(Pos.BASELINE_CENTER);
        populateCalendar(yearMonth);
        //view del calendario
        view = new VBox(titleBar, dayLabels, calendar);
    }

    /**
     * Riempie il calendario con i giorni settati in modo opportuno.
     *
     * @param yearMonth mese e anno da popolare con i corretti giorni
     */
    public void populateCalendar(YearMonth yearMonth) {
        //la data con cui vogliamo iniziare a visualizzare il calendario
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);

        // ritaglia giorno fino a che sia DOMENICA (a meno che il mese non inizi di domenica)
        while (!calendarDate.getDayOfWeek().toString().equals("MONDAY")) {
            calendarDate = calendarDate.minusDays(1);
        }

        // riempie il calendario con il numero dei giorni
        for (AnchorPaneNode ap : allCalendarDays) {
            if (ap.getChildren().size() != 0) {
                ap.getChildren().remove(0);
                ap.getChildren().clear();  //serve per il refresh
            }

            //le righe sotto servono a far spuntare i cerchi nelle celle del calendario:
            Label txt = new Label(String.valueOf(calendarDate.getDayOfMonth()));
            LocalDate dayCalendar = LocalDate.of(calendarDate.getYear(), calendarDate.getMonthValue(), calendarDate.getDayOfMonth());


            Circle circle = new Circle(); //inserimento eventi
            circle.setRadius(30.0);
            circle.setStroke(Color.TRANSPARENT);
            circle.setStrokeWidth(1);
            circle.setStrokeType(StrokeType.INSIDE);
            circle.setEffect(new DropShadow(+2d, 0d, +2d, Color.LIGHTBLUE));


            // si deve colorare il cerchio in base al numero di visite in un giorno e settare il testo all'interno
            //:deve spuntare numero di visite in un giorno di quel giorno per l'utente loggato OK FATTO

            String id_doctor = this.doctorRepo.search(SessionUser.getDoctor());
            Integer countVisitDay = this.appointmentRepo.countAppointmentsByDateAndDoctor(dayCalendar.toString(), id_doctor);
            //Integer countVisitDay = this.appointmentRepo.countAppointmentsByDate(dayCalendar.toString()); //questo è generale NON Cancellare
            if (countVisitDay == 0)
                circle.setFill(Color.WHITE);
            else if (countVisitDay < 3)
                circle.setFill(Color.YELLOW);
            else
                circle.setFill(Color.RED);

            Text text = new Text(countVisitDay.toString());
            text.setFont(Font.font("Calibre", 15));
            text.setBoundsType(TextBoundsType.VISUAL);
            StackPane stack = new StackPane();
            stack.getChildren().add(circle);
            stack.getChildren().add(text);
            stack.setId("stack");


            setTopAnchor(stack, 40.0);
            setLeftAnchor(stack, 30.0);
            ap.getChildren().add(stack);
            ap.setDate(calendarDate);
            txt.setFont(Font.font("Calibri", 15));
            txt.setPrefWidth(100);

            txt.setStyle("-fx-text-fill: white; -fx-alignment: center; -fx-background-color: #3DA4E3;");
            setTopAnchor(txt, 5.0);
            setLeftAnchor(txt, 5.0);

            ap.getChildren().add(txt);
            ap.setStyle("-fx-background-color: white; -fx-border-color:#163754;");
            calendarDate = calendarDate.plusDays(1);
        }

        calendarTitle.setStyle("-fx-text-fill: #163754");
        //Cambia titolo al calendario (stringa in alto)
        calendarTitle.setText(yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN).toUpperCase(Locale.ROOT) + "  " + yearMonth.getYear());
    }

    /**
     * Metodo che sposta il mese indietro di uno. Ripopola il calendario con le date corrette.
     */
    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        populateCalendar(currentYearMonth);
    }

    /**
     * Metodo che sposta il mese in avanti di uno. Ripopola il calendario con le date corrette.
     */
    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        //System.out.println(currentYearMonth);
        populateCalendar(currentYearMonth);
    }

    /**
     * Metodo che ritorna la view
     *
     * @return ritorna il VBox
     */
    public VBox getView() {
        return view;
    }

}
