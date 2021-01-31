package controller.factorySidebar;

import javafx.scene.control.Button;

import java.util.List;

public class AdminSideBar implements SideBarAction{
    private final Button aggiungi = new Button("Aggiungi");
    private final Button dottori = new Button("Dottori");
    private final Button segreteria = new Button("Segreteria");
    private final Button clinica = new Button("Clinica");
    private final Button logout = new Button("Logout");
    @Override
    public List<Button> getSpecificAction() {
        return List.of(aggiungi,dottori, segreteria, clinica,logout);
    } //aggiungere qui nuovi comandi per l'ammistratore
}
