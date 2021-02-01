package util.email;

import javafx.scene.control.Alert;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe  ServiceClientMail {@link ServiceClientMail}  responsabile dell'invio dell'email.
 */
public class ServiceClientMail {
    private static JFrame jfrProgress;
    private static JProgressBar progressBar;
    private static String doing;

    /**
     * Metodo che manda l'email all'owner associato alla visita
     *
     * @param email oggetto Communication {@link Communication} con tutti i parametri utili all'invio dell'email
     * @param host  imposta il server da utilizzare per l'invio dell' email
     */
    public static void sendMail(Communication email, String host) {

        jfrProgress = new JFrame("Invio email in corso......");
        Container contentPane = jfrProgress.getContentPane();
        contentPane.setLayout(null);
        jfrProgress.setLocation(700, 350);
        jfrProgress.setSize(600, 150);
        jfrProgress.setVisible(true);
        // Tries To Make Bar Close On Exit But Bar Sometimes Too Fast
        jfrProgress.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Bar Progress Counter
        int progress = 100;
        // Make Bar
        progressBar = new JProgressBar(0, progress);
        doing = "Invio email in corso... 0 %";
        jfrProgress.getContentPane().add(progressBar, BorderLayout.CENTER);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setString(doing);
        progressBar.setSize(600, 150);
        progressBar.setVisible(true);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.paintImmediately(0, 0, jfrProgress.getWidth(), jfrProgress.getHeight());

        Properties proprieties = new Properties();

        //necessari per mandare l'email
        proprieties.put("mail.smtp.auth", "true");
        proprieties.put("mail.smtp.starttls.enable", "true");
        proprieties.put("mail.smtp.host", host);  //mando  email con dominio gmail.com
        proprieties.put("mail.smtp.port", "587");


        String myAccountEmail = email.getMyAccountEmail();
        String myPassword = email.getMyPassword();

        //login usando l'email del sender account
        Session session = Session.getInstance(proprieties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, myPassword);
            }
        });


        Message message = prepareMessage(session, myAccountEmail, email);
        try {
            // Progress ++;
            progressBar.setValue(66);
            doing = "Invio email a " + email.getReceiver() + " in corso... 66 %";
            progressBar.setString(doing);
            progressBar.paintImmediately(0, 0, jfrProgress.getWidth(), jfrProgress.getHeight());

            assert message != null;
            Transport.send(message);

            // Progress ++;
            progressBar.setValue(100);
            progressBar.paintImmediately(0, 0, jfrProgress.getWidth(), jfrProgress.getHeight());

            //chiudo progress bar
            jfrProgress.dispose();
            //JOptionPane.showMessageDialog(null ,"L'email è stata inviata correttamente!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Email Inviata Correttamente all'indirizzo email: " + email.getReceiver());
            alert.setTitle("Email correttamente inviata");
            alert.showAndWait();

            System.out.println("Inviata!");
        } catch (MessagingException ex) {
            JOptionPane.showMessageDialog(null, "Errore: L'email non è stata inviata!");
            System.out.println("Errore invio!");
        }
    }

    /**
     * Metodo che prepara il messaggio da inviare nell'email all'owner associato alla visita
     *
     * @param session        la sessione autenticata per mandare il messaggio
     * @param myAccountEmail l'indirizzo email con cui viene mandato il messaggio
     * @param email          oggetto Communication {@link Communication} con tutti i parametri utili all'invio dell'email
     */
    private static Message prepareMessage(Session session, String myAccountEmail, Communication email) {
        try {
            // Progress ++;
            progressBar.setValue(33);
            doing = "Invio email a " + email.getReceiver() + " in corso... 33%";
            progressBar.setString(doing);
            progressBar.paintImmediately(0, 0, jfrProgress.getWidth(), jfrProgress.getHeight());
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getReceiver()));
            message.setSubject("IMPORTANTE: Notifica ");
            message.setText("Volevamo avvisarti che: " +
                    "\nLa visita programmata sarà giorno: " + email.getDataVisit().toString() +
                    "\ndalle " + email.getTimeStartVisit().toString() + " alle " + email.getTimeEndVisit().toString() + " (ora fine prevista)" +
                    "\nCordiali saluti, \nStaff VetClinicManagement");
            return message;
        } catch (MessagingException ex) {
            Logger.getLogger(ServiceClientMail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
