package util;
import javafx.scene.control.Alert;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServiceMail {
    public static void sendMail(String receiver) {

        System.out.println("Prepazione invio email in corso... ");
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Prepazione invio email in corso...");
        alert.setTitle("Prepazione invio email in corso...!");
        alert.showAndWait();

        Properties proprieties = new Properties();

        //necessari per mandare l'email
        proprieties.put("mail.smtp.auth", "true");
        proprieties.put("mail.smtp.starttls.enable", "true");
        proprieties.put("mail.smtp.host", "smtp.gmail.com");  //mando ad email con dominio gmail.com
        proprieties.put("mail.smtp.port", "587");



        //sender account
        String myAccountEmail = "vetclinicmanagement@gmail.com";  //ACCOUNT PREDEFINITO della clinica
        String myPassword = "VetClinicApp2021";

        //login usando l'email del sender account
        Session session = Session.getInstance(proprieties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, myPassword);
            }
        });


        Message message = prepareMessage(session, myAccountEmail, receiver);
        try{

            Transport.send(message);
            JOptionPane.showMessageDialog(null ,"L'email è stata inviata correttamente!");
            System.out.println("Inviata!");
        }catch (MessagingException ex){
            JOptionPane.showMessageDialog(null ,"Errore: L'email non è stata inviata!");
            System.out.println("Errore invio!");
        }
    }

    private static Message prepareMessage(Session session, String myAccountEmail,  String receiver){
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject("IMPORTANTE: Notifica Variazione");
            message.setText("Volevamo avvisarti che ci sono stati cambiamenti \nCordiali saluti, \nStaff VetClinicManagement");
            return  message;
        } catch (MessagingException ex){
            Logger.getLogger(ServiceMail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
