package controller;

import dao.AppointmentDAO;
import dao.ConcreteDoctorDAO;
import dao.ConcreteSecretariatDAO;
import datasource.ConnectionDBH2;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import util.SessionUser;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class PersonalProfileController implements Initializable{
    public ImageView picProfile;
    public Button profilePicButton;
    public CheckBox checkboxUpdate;
    public HBox form_pain_main;
    public TextField name;
    public TextField surname;
    public HBox gender;
    public RadioButton rbM;
    public RadioButton rbF;
    public DatePicker birthDate;
    public TextField city;
    public TextField address;
    public TextField telephone;
    public TextField fiscalCode;
    public TextField email;
    public TextField username;
    private final ConcreteSecretariatDAO secretariatRepo;
    private final ConcreteDoctorDAO doctorRepo;
    private Doctor doctor;
    private Secretariat secretariat;


    public PersonalProfileController(String roleUserLogged){
        this.secretariatRepo = new ConcreteSecretariatDAO(ConnectionDBH2.getInstance());
        this.doctorRepo = new ConcreteDoctorDAO(ConnectionDBH2.getInstance());

        switch (roleUserLogged){
            case "Dottore" -> {
                String id_doctor = this.doctorRepo.search(SessionUser.getDoctor());
                this.doctor = this.doctorRepo.searchById(id_doctor);
            }
            case "Segreteria" -> {
                String id_secretariat = this.secretariatRepo.search(SessionUser.getSecretariat());
                this.secretariat = this.secretariatRepo.searchById(id_secretariat);
            }
            default -> throw new IllegalStateException("Unexpected value");
        }

    }

    public void changeProfilePic() throws FileNotFoundException {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            String filePath = file.getAbsolutePath();
            if(filePath.endsWith(".jpg") ||
                    filePath.endsWith(".jpeg") ||
                    filePath.endsWith(".png") ||
                    filePath.endsWith(".gif")) {
                InputStream stream = new FileInputStream(filePath);
                try {
                    stream = new FileInputStream("D:\\images\\elephant.jpg");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Image image = new Image(stream);
                //Setting image to the image view
                this.picProfile.setImage(image);
            }
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
        if(doctor != null){
            setParamDoctor(this.doctor);
        }else if(secretariat != null){
            setParamSecretariat(this.secretariat);
        }
    }

    public void setParamDoctor(Doctor data) {
        this.name.setText(data.getName().trim());
        this.surname.setText(data.getSurname().trim());
        this.address.setText(data.getAddress().trim());
        this.city.setText(data.getCity().trim());
        this.telephone.setText(data.getTelephone().trim());
        this.email.setText(data.getEmail().trim());
        this.fiscalCode.setText(data.getFiscalCode().trim());
        this.birthDate.setValue(data.getDatebirth());
        if(data.getSex().compareTo(Gender.M) == 0){
            this.rbM.setSelected(true);
        }else {
            this.rbF.setSelected(true);
        }
       //this.specialization.setValue(data.getSpecialization().trim());
       this.username.setText(data.getUsername().trim());
    }

    public void setParamSecretariat(Secretariat data) {
        this.name.setText(data.getName().trim());
        this.surname.setText(data.getSurname().trim());
        this.address.setText(data.getAddress().trim());
        this.city.setText(data.getCity().trim());
        this.telephone.setText(data.getTelephone().trim());
        this.email.setText(data.getEmail().trim());
        this.fiscalCode.setText(data.getFiscalCode().trim());
        this.birthDate.setValue(data.getDatebirth());
        if(data.getSex().compareTo(Gender.M) == 0){
            this.rbM.setSelected(true);
        }else {
            this.rbF.setSelected(true);
        }
        this.username.setText(data.getUsername().trim());
    }

}