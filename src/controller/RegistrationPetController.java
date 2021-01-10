package controller;

import dao.ConcreteOwnerDAO;
import dao.ConcretePetDAO;
import datasource.ConnectionDBH2;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Gender;
import model.Owner;
import model.Pet;

import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RegistrationPetController implements Initializable {
    public VBox pane_main_grid;
    public Label labelTitle;
    public TextField textName;
    public TextField textSurname;
    public HBox gender;
    public RadioButton rbM;
    public ToggleGroup genderGroup;
    public RadioButton rbF;
    public DatePicker textdateBirth;
    public ComboBox textPetRace;
    public ComboBox textOwner;
    public TextField textParticularSign;
    public Button btn;
    private ConcretePetDAO petRepo;

    public RegistrationPetController() {
        this.rbM = new RadioButton(Gender.M.getDeclaringClass().descriptorString());
        this.rbF = new RadioButton(Gender.F.getDeclaringClass().descriptorString());

        try{
            ConnectionDBH2 connection = new ConnectionDBH2();
            this.petRepo = new ConcretePetDAO(connection);

        }

        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       addFieldOwner();
       addFieldRace();
       addButtonSave();
       addActionButton();

    }
    public  void addButtonSave()  {
        this.btn = new Button("Salva");
        this.btn.setId("btn");
        this.pane_main_grid.getChildren().add(this.btn);
    }

    public void addActionButton() {
        this.btn.setOnAction(this::registrationPet);
    }

    public void registrationPet(ActionEvent actionEvent) {
             Pet p = createPet();

            //inserire controlli
            this.petRepo.add(p);

        System.out.println(p.getName());
        System.out.println(p.getSurname());
        System.out.println(p.getDatebirth());
        System.out.println(p.getSex());
        System.out.println(p.getId_petRace());
        System.out.println(p.getId_owner());
        System.out.println(p.getParticularSign());

    }

    public Pet createPet(){
        RadioButton chk = (RadioButton)this.genderGroup.getSelectedToggle();
        System.out.println(chk.getText());

        Pet p = new Pet.Builder<>((String) this.textPetRace.getValue(),
                (String) this.textOwner.getValue(),
                this.textParticularSign.getText())
                .addName(this.textName.getText())
                .addSurname(this.textSurname.getText())
//                .addSex((chk.getText().equals("M") ? Gender.M : Gender.F).toString())
                .addDateBirth(this.textdateBirth.getValue())
                .build();

        return p;
    }

    public void addFieldOwner()  {
        List<String> listClient = this.petRepo.searchAllSurnameClient();

        this.textOwner = new ComboBox(FXCollections
                .observableArrayList(listClient));

        this.textOwner.setId("owner");
        this.textOwner.setPromptText("Cliente");
        this.pane_main_grid.getChildren().add(this.textOwner);
    }
    public void addFieldRace()  {
        List<String> listRace =this.petRepo.searchAllRace();
        this.textPetRace = new ComboBox(FXCollections
                .observableArrayList(listRace));
        this.textPetRace.setId("petRace");
        this.textPetRace.setPromptText("Razza");
        this.pane_main_grid.getChildren().add(this.textPetRace);
    }
}
