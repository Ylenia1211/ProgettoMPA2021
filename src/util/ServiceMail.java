package util;
import javafx.scene.control.Alert;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServiceMail {
    private static JFrame jfrProgress;
    private static Container contentPane;
    private static JProgressBar progressBar;
    private static String doing;
    public static void sendMail(String receiver, LocalDate dataVisit, LocalTime timeStartVisit, LocalTime timeEndVisit) {

        System.out.println("Prepazione invio email in corso... ");
        /*Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Prepazione invio email in corso...");
        alert.setTitle("Prepazione invio email in corso...!");
        alert.showAndWait(); */

       // Make New Frame For Bar
        jfrProgress = new JFrame("Invio email in corso......");
        contentPane = jfrProgress.getContentPane();
        contentPane.setLayout(null);
        jfrProgress.setLocation(700,350);
        jfrProgress.setSize(600,150);
        jfrProgress.setVisible(true);
        // Tries To Make Bar Close On Exit But Bar Sometimes Too Fast
        jfrProgress.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Bar Progress Counter
        int progress = 100;
        // Make Bar
        progressBar = new JProgressBar(0, progress);
        doing = "Invio email in corso... 0 %";
        jfrProgress.getContentPane().add(progressBar,BorderLayout.CENTER);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setString(doing);
        progressBar.setSize(600, 150);
        progressBar.setVisible(true);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.paintImmediately(0,0,jfrProgress.getWidth(),jfrProgress.getHeight());


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


        Message message = prepareMessage(session, myAccountEmail, receiver, dataVisit, timeStartVisit, timeEndVisit);
        try{
            // Progress ++;
            progressBar.setValue(66);
            doing = "Invio email in corso... 66 %";
            progressBar.setString(doing);
            progressBar.paintImmediately(0,0,jfrProgress.getWidth(),jfrProgress.getHeight());

            Transport.send(message);

            // Progress ++;
            progressBar.setValue(100);
            progressBar.paintImmediately(0,0,jfrProgress.getWidth(),jfrProgress.getHeight());

            //Close
            jfrProgress.dispose();
            //JOptionPane.showMessageDialog(null ,"L'email è stata inviata correttamente!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Email Inviata!");
            alert.setTitle("Email correttamente inviata");
            alert.showAndWait();

            System.out.println("Inviata!");
        }catch (MessagingException ex){
            JOptionPane.showMessageDialog(null ,"Errore: L'email non è stata inviata!");
            System.out.println("Errore invio!");
        }
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String receiver, LocalDate dataVisit, LocalTime timeStartVisit, LocalTime timeEndVisit){
        try {
            // Progress ++;
            progressBar.setValue(33);
            doing = "Invio email in corso... 33%";
            progressBar.setString(doing);
            progressBar.paintImmediately(0,0,jfrProgress.getWidth(),jfrProgress.getHeight());
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject("IMPORTANTE: Notifica Variazione");
            message.setText("Volevamo avvisarti che ci sono stati cambiamenti: " +
                     "\n La visita programmata sarà giorno: " + dataVisit.toString() +
                     "\n dalle " + timeStartVisit.toString() + " alle " + timeEndVisit.toString() +" (ora fine prevista)"+
                    " \nCordiali saluti, \nStaff VetClinicManagement");
            return  message;
        } catch (MessagingException ex){
            Logger.getLogger(ServiceMail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
