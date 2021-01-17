import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private double x, y;

    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(Main.class.getResource("/view/doctorDashboard.fxml"));
        Parent root = FXMLLoader.load(Main.class.getResource("/view/login.fxml")); //ok funzionante bisogna sistemare solo le azioni specifiche della sidebar

        root.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - x);
            stage.setY(mouseEvent.getScreenY() - y);
        });

        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.TRANSPARENT);
        //stage.setMaximized(true);
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
