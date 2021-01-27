package controller.factorySidebar;

import javafx.scene.control.Button;

import java.util.List;

public class AdminSideBar implements SideBarAction{
    private Button aggiungi = new Button("Aggiungi");
    private Button dottori = new Button("Dottori");
    private Button segreteria = new Button("Segreteria");
    private Button clinica = new Button("Clinica");
    public Button logout = new Button("Logout");
    @Override
    public List<Button> getSpecificAction() {
        return List.of(aggiungi,dottori, segreteria, clinica,logout);
    } //aggiungere qui nuovi comandi per l'ammistratore
}
