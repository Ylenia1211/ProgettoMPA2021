package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import static javafx.scene.layout.AnchorPane.*;

public class FullCalendarView {

    private ArrayList<AnchorPaneNode> allCalendarDays = new ArrayList<>(35);
    private final VBox view;
    private final Text calendarTitle;
    private YearMonth currentYearMonth;

    /**
     * Create a calendar view
     * @param yearMonth year month to create the calendar of
     */
    public FullCalendarView(YearMonth yearMonth) {
        currentYearMonth = yearMonth;
        // Create the calendar grid pane
        GridPane calendar = new GridPane();
        calendar.setPrefSize(800, 600);
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
        Text[] dayNames = new Text[]{new Text("Monday"), new Text("Tuesday"),
                                        new Text("Wednesday"), new Text("Thursday"), new Text("Friday"),
                                        new Text("Saturday"),  new Text("Sunday") };
        GridPane dayLabels = new GridPane();
        dayLabels.setPrefWidth(600);
        int col = 0;
        for (Text txt : dayNames) {
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(200, 10);
            setBottomAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            dayLabels.add(ap, col++, 0);
        }
        // Create calendarTitle and buttons to change current month
        calendarTitle = new Text();
        Button previousMonth = new Button("<<");
        previousMonth.setOnAction(e -> previousMonth());
        Button nextMonth = new Button(">>");
        nextMonth.setOnAction(e -> nextMonth());
        HBox titleBar = new HBox(previousMonth, calendarTitle, nextMonth);
        titleBar.setAlignment(Pos.BASELINE_CENTER);
        // Populate calendar with the appropriate day numbers
        populateCalendar(yearMonth);
        // Create the calendar view
        view = new VBox(titleBar, dayLabels, calendar);
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
            // si deve colorare il cerchio in base al numero di visite in un giorno e settare il testo all'interno
            Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            Circle circle = new Circle(); //inserimento eventi
            circle.setRadius(30.0);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(2);
            circle.setStrokeType(StrokeType.INSIDE);
            circle.setFill(Color.WHITE);


            Text text = new Text("#visite");

            text.setBoundsType(TextBoundsType.VISUAL);
            StackPane stack = new StackPane();
            stack.getChildren().add(circle);
            stack.getChildren().add(text);
            setTopAnchor(stack, 30.0);
            setLeftAnchor(stack, 30.0);
            ap.getChildren().add(stack);

            ap.setDate(calendarDate);

            setTopAnchor(txt, 5.0);
            setLeftAnchor(txt, 5.0);
            ap.getChildren().add(txt);



            //ap.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            calendarDate = calendarDate.plusDays(1);
        }
        // Change the title of the calendar
        calendarTitle.setText(yearMonth.getMonth().toString() + " " + yearMonth.getYear());
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
