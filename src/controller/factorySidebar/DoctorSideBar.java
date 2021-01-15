package controller.factorySidebar;

import javafx.scene.control.Button;

import java.util.List;

public class DoctorSideBar implements SideBarAction {
    //bottoni per la sidebar del dottore
    private Button agenda = new Button("Agenda");


    @Override
    public List<Button> getSpecificAction() {
        return List.of(this.agenda);  ////aggiungere qui nuovi comandi per dottore
    }
}
