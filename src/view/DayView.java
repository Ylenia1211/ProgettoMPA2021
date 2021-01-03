package view;

import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;


public class DayView implements Initializable {
    public AnchorPane fxDayView;
    public TextField dayInfo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void getDay(String day) {
        dayInfo.setText(day);
    }
}
