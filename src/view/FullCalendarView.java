package view;

import dao.ConcreteAppointmentDAO;
import datasource.ConnectionDBH2;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import model.Appointment;

import javax.swing.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static javafx.scene.layout.AnchorPane.*;

public class FullCalendarView {

    private ArrayList<AnchorPaneNode> allCalendarDays = new ArrayList<>(35);
    private final VBox view;
    private final Text calendarTitle;
    private YearMonth currentYearMonth;
    private ConcreteAppointmentDAO appointmentRepo;
    private List<Appointment> listAppointmentDay;
    /**
     * Create a calendar view
     * @param yearMonth year month to create the calendar of
     */
    public FullCalendarView(YearMonth yearMonth) {

        try{
            ConnectionDBH2 connection = new ConnectionDBH2();
            this.appointmentRepo = new ConcreteAppointmentDAO(connection);
            this.listAppointmentDay = new ArrayList<>();
        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }

        currentYearMonth = yearMonth;
        //System.out.println(currentYearMonth);
        // Create the calendar grid pane
        GridPane calendar = new GridPane();
        calendar.setPrefSize(800, 600); //dimensione agenda
        calendar.setGridLinesVisible(true);
        // Create rows and columns with anchor panes for the calendar
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                AnchorPaneNode ap = new AnchorPaneNode();
                ap.setPrefSize(200,200);
                calendar.add(ap,j,i);
                allCalendarDays.add(ap);
            }
        }
        // Days of the week labels
        Text[] dayNames = new Text[]{new Text("Lunedi"), new Text("Martedi"),
                                        new Text("Mercoledi"), new Text("Giovedi"), new Text("Venerdi"),
                                        new Text("Sabato"),  new Text("Domenica") };
        GridPane dayLabels = new GridPane();
        dayLabels.setPrefWidth(600);
        int col = 0;
        for (Text txt : dayNames) {
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(200, 10);
            txt.setFont(Font.font("Calibri", 20));
            setBottomAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            dayLabels.add(ap, col++, 0);
        }
        // Create calendarTitle and buttons to change current month
        calendarTitle = new Text();
        calendarTitle.setFont(Font.font("Calibre",15));
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
        // Populate calendar with the appropriate day numbers
        populateCalendar(yearMonth);
        // Create the calendar view
        view = new VBox(titleBar, dayLabels, calendar);
        //view = new VBox( containertitle,titleBar, dayLabels, calendar);
    }

    /**
     * Set the days of the calendar to correspond to the appropriate date
     * @param yearMonth year and month of month to render
     */
    public void populateCalendar(YearMonth yearMonth) {
        // Get the date we want to start with on the calendar
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        // Dial back the day until it is SUNDAY (unless the month starts on a sunday)
        while (!calendarDate.getDayOfWeek().toString().equals("MONDAY") ) {
            calendarDate = calendarDate.minusDays(1);
        }
        // Populate the calendar with day numbers
        for (AnchorPaneNode ap : allCalendarDays) {
            if (ap.getChildren().size() != 0) {
                ap.getChildren().remove(0);
                ap.getChildren().clear();  //serve per il refresh
            }

            // #TODO le righe sotto servono a far spuntare i cerchi nelle celle del calendario:

            Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            LocalDate dayCalendar = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), calendarDate.getDayOfMonth());
            //this.appointmentRepo.searchAppointmentsByDate(String.valueOf(calendarDate.getDayOfMonth()));


            Circle circle = new Circle(); //inserimento eventi
            circle.setRadius(30.0);
            circle.setStroke(Color.TRANSPARENT);
            circle.setStrokeWidth(1);
            circle.setStrokeType(StrokeType.INSIDE);
            circle.setEffect(new DropShadow(+2d, 0d, +2d, Color.LIGHTBLUE));


            // si deve colorare il cerchio in base al numero di visite in un giorno e settare il testo all'interno
            Integer countVisitDay = this.appointmentRepo.countAppointmentsByDate(String.valueOf(dayCalendar));
            if(countVisitDay == 0)
                circle.setFill(Color.WHITE);
            else  if(countVisitDay < 3)
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


            txt.setFont(Font.font("Calibre", 15));

            setTopAnchor(txt, 5.0);
            setLeftAnchor(txt, 5.0);
            ap.getChildren().add(txt);

            //ap.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            calendarDate = calendarDate.plusDays(1);
        }
        // Change the title of the calendar
        calendarTitle.setText(yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN).toUpperCase(Locale.ROOT)+ "  " + yearMonth.getYear()); //#TODO: qua cambiare la stampa del mese in italiano
    }

    /**
     * Move the month back by one. Repopulate the calendar with the correct dates.
     */
    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        populateCalendar(currentYearMonth);
    }

    /**
     * Move the month forward by one. Repopulate the calendar with the correct dates.
     */
    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        populateCalendar(currentYearMonth);
    }

    public VBox getView() {
        return view;
    }

    public ArrayList<AnchorPaneNode> getAllCalendarDays() {
        return allCalendarDays;
    }

    public void setAllCalendarDays(ArrayList<AnchorPaneNode> allCalendarDays) {
        this.allCalendarDays = allCalendarDays;
    }
}
