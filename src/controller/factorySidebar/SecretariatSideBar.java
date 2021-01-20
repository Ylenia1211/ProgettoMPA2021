package controller.factorySidebar;

import javafx.scene.control.Button;

import java.util.List;

public class SecretariatSideBar implements SideBarAction{
    public Button logout = new Button("Logout");
    @Override
    public List<Button> getSpecificAction() {
        return List.of(logout);//aggiungere qui nuovi comandi per segretaria
    }
}
