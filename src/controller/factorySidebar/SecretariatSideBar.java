package controller.factorySidebar;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;
import javax.swing.text.Position;
import java.util.List;

public class SecretariatSideBar implements SideBarAction{
    public Button add = new Button("Aggiungi");
    public Button profilo = new Button("Profilo");
    public Button pazienti = new Button("Pazienti");
    public Button clienti = new Button("Clienti");
    public Button prenotazioni = new Button("Prenotazioni");

    public Button notifica = new Button( "Notifica" );
    public Button logout = new Button("Logout");
    @Override
    public List<Button> getSpecificAction() {
        final ImageView imageView = new ImageView(
                new Image("./notification.png")
        );
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        notifica = new Button("Notifica", imageView);
        return List.of(add,clienti,pazienti,prenotazioni,notifica, profilo,logout);//aggiungere qui nuovi comandi per segretaria
    }
}
