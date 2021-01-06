package controller;

import dao.ConcreteDoctorDAO;
import dao.ConcreteOwnerDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import model.Doctor;
import model.Gender;
import model.Owner;

import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RegistrationDoctorController extends ClientController{
    private TextField username;
    private PasswordField password;
    private ComboBox specialitation;
    private Button saveBtn;
    private ConcreteDoctorDAO doctorRepo;

    public RegistrationDoctorController() {
        //ConnectionDBH2.quitConnectionDB();
        try{

            ConnectionDBH2 connection = new ConnectionDBH2();
            this.doctorRepo = new ConcreteDoctorDAO(connection);
            //System.out.println(this.doctorRepo);
        }

        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        super.initialize(url, resourceBundle);

        Label l = (Label) super.pane_main_grid.lookup("#labelTitle");
        l.setText("Creazione Dottore");
        super.pane_main_grid.getChildren().remove(btn); //per rimuovere da pannello dinamicamente il bottone di salvataggio

        // menu a tendina per specializzazione
        addFieldSpecialitation();
        //username, password
        addFieldUsername();
        addFieldPassword();
        addButtonSave();
        addActionButton();


    }
    public void addFieldSpecialitation()  {
        List<String> listSpecialitation = this.getDoctorRepo().searchAllSpecialization();
        this.specialitation = new ComboBox(FXCollections
                .observableArrayList(listSpecialitation));
        this.specialitation.setId("specialitation");
        this.specialitation.setPromptText("Scegli specializzazione");
        super.pane_main_grid.getChildren().add(this.specialitation);
    }


    public void addFieldUsername()  {
        this.username = new TextField();
        this.username.setId("username");
        this.username.setPromptText("Username");
        super.pane_main_grid.getChildren().add(this.username);
    }

    public  void addFieldPassword()  {
        this.password = new PasswordField();
        this.password.setId("password");
        this.password.setPromptText("Inserisci password Utente");
        super.pane_main_grid.getChildren().add(this.password);
    }

    public  void addButtonSave()  {
        this.saveBtn = new Button("Salva");
        this.saveBtn.setId("saveBtn");
        super.pane_main_grid.getChildren().add(this.saveBtn);
        }

    public void addActionButton() {
            this.saveBtn.setOnAction(e -> {
                /*
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Devi implementare la creazione del Dottore");
                alert.show();
                */
                Doctor d = createDoctor();
               // prova
                /*
                System.out.println(d.getName());
                System.out.println(d.getSurname());
                System.out.println(d.getDatebirth());
                System.out.println(d.getSex());
                System.out.println(d.getAddress());
                System.out.println(d.getCity());
                System.out.println(d.getTelephone());
                System.out.println(d.getEmail());
                System.out.println(d.getUsername());
                System.out.println(d.getPassword());
                System.out.println(d.getSpecialitation());
                */


                //inserire controlli
                this.doctorRepo.add(d);
            });



    }

    public ConcreteDoctorDAO getDoctorRepo() {
        return doctorRepo;
    }

    public Doctor createDoctor(){
        RadioButton chk = (RadioButton)this.genderGroup.getSelectedToggle();
        System.out.println(chk.getText());

        Doctor p = new Doctor.Builder<>(
                (String) this.specialitation.getValue(),
                this.username.getText(),
                this.password.getText())
                .addName(super.getTextName().getText())
                .addSurname(super.getTextSurname().getText())
                .addSex((chk.getText().equals("M") ? Gender.M : Gender.F).toString())
                .addDateBirth(super.getTextdateBirth().getValue())
                .addAddress(super.getTextAddress().getText())
                .addCity(super.getTextCity().getText())
                .addTelephone(super.getTextTelephone().getText())
                .addEmail(super.getTextEmail().getText())
                .build();

        return p;
    }
}