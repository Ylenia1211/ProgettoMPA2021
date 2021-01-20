package controller.factorySidebar;

import javafx.scene.control.Button;

import java.util.List;

public class AdminSideBar implements SideBarAction{
    private Button utenti = new Button("Utenti");
    public Button logout = new Button("Logout");
    @Override
    public List<Button> getSpecificAction() {
        return List.of(utenti,logout);
    } //aggiungere qui nuovi comandi per l'ammistratore
}
