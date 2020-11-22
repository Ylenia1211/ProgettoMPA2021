import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = (Parent) FXMLLoader.load(Main.class.getResource("/view/registrationClient.fxml"));
        stage.setTitle("App Clinic");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
        stage.setResizable(false);
    }
    public static void main(String[] args) throws ClassNotFoundException {
        launch(args);
    }
}
