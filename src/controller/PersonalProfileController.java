package controller;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Doctor;
import model.Gender;
import model.Person;
import model.Secretariat;
import javax.activation.MimetypesFileTypeMap;
import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class PersonalProfileController implements Initializable{
//    private final String id;
//    public Button profilePicButton;
//    public TextField name;
//    public TextField surname;
//    public HBox gender;
//    public RadioButton rbM;
//    public ToggleGroup genderGroup;
//    public RadioButton rbF;
//    public DatePicker birthDate;
//    public TextField city;
//    public TextField address;
//    public TextField telephone;
//    public TextField fiscalCode;
//    public TextField email;
//    public TextField username;
//    public PasswordField password;
//    public TextField newPassword;
//    public Button update;
//    public VBox labelsFields;
//    public VBox labels;
    public ImageView picProfile;

//    public PersonalProfileController(String id, Person person) {
////        this.id = id;
//        Doctor doctor;
//        Secretariat secretariat;
//        if (person instanceof Doctor)
//            doctor = (Doctor) person;
//        else if (person instanceof Secretariat)
//            secretariat = (Secretariat) person;
//    }

    public void changeProfilePic() {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            String filePath = file.getAbsolutePath();
            File f = new File(filePath);
            String mimetype= new MimetypesFileTypeMap().getContentType(f);
            String type = mimetype.split("/")[0];
            if(type.equals("image"))
                this.picProfile.setImage(new Image(filePath));
            else
                JOptionPane.showMessageDialog(null, "Inserire un'immagine valida");
        }else
            JOptionPane.showMessageDialog(null, "Inserire un'immagine valida");
    }

    public void updateProfile() {
        // #Todo: Aggiornare il db solo se si rilevano aggiornamenti in almeno un campo.
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //this.setParam(this.person);
    }

    public void setParam(Person data) {
//        this.name.setText(data.getName().trim());
//        this.surname.setText(data.getSurname().trim());
//        this.address.setText(data.getAddress().trim());
//        this.city.setText(data.getCity().trim());
//        this.telephone.setText(data.getTelephone().trim());
//        this.email.setText(data.getEmail().trim());
//        this.fiscalCode.setText(data.getFiscalCode().trim());
//        this.birthDate.setValue(data.getDatebirth());
//        if(data.getSex().compareTo(Gender.M) == 0){
//            this.rbM.setSelected(true);
//        }else {
//            this.rbF.setSelected(true);
//        }
////        #TODO: SE SARà UN DOTTORE VERRANNO AGGIUNTI ANCHE QUESTI IN BASSO
////        this.specialization.setValue(data.getSpecialization().trim());
////        this.username.setText(data.getUsername().trim());
////        super.getPassword().setText(data.getPassword().trim());
    }

    public void addSpecialization() {

    }
}
