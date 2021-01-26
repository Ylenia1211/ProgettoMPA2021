package controller.factorySidebar;

import javafx.scene.control.Button;

import java.util.List;

public class SecretariatSideBar implements SideBarAction{
    public Button profilo = new Button("Profilo");
    public Button pazienti = new Button("Pazienti");
    public Button clienti = new Button("Clienti");
    public Button prenotazioni = new Button("Prenotazioni");
    public Button logout = new Button("Logout");
    @Override
    public List<Button> getSpecificAction() {
        return List.of(clienti,pazienti,prenotazioni,profilo,logout);//aggiungere qui nuovi comandi per segretaria
    }
}
