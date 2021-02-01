import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per l'avvio della Standalone Application, che estende la classe 'Application' {@link Application}
 */
public class Main extends Application {

    private double x, y;

    /**
     * Il punto di ingresso principale per tutte le applicazioni JavaFX.
     * In questo caso setta la View iniziale che si avvierà nel momento in cui verrà lanciata la StandAlone Application.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("/view/login.fxml"));

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
        stage.show();
        stage.setResizable(false);
    }

    /**
     * Funzione main lancia la standalone application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
