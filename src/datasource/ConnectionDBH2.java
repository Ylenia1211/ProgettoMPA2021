package datasource;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe che permette la connessione con il database H2.
 */
public class ConnectionDBH2 {
    public String name_db = "dataBaseH2";
    private static Connection connectData = null;

    //SINGLETON
    private static final ConnectionDBH2 instance = new ConnectionDBH2();

    /**
     * Metodo Costruttore Singleton della connessione con il database H2.
     * Permette la connessione al driver manager utilizzando url, username, e password del db.
     */
    private ConnectionDBH2() {
        try {
            Class.forName("org.h2.Driver");
            String username = "";
            String password = "";
            String url = "jdbc:h2:./h2/bin/database/" + name_db + ";IFEXISTS=TRUE";
            connectData = DriverManager.getConnection(url, username, password);
            System.out.println("DB connected!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
        }
    }

    /**
     * Metodo che prende la connessione del database H2 per il dialogo con le tabelle del db.
     */
    public Connection getConnectData() {
        return connectData;
    }

    /**
     * Metodo che prende l'istanza della connessione con il database H2, univoca per tutta l'esecuzione del programma.
     */
    public static ConnectionDBH2 getInstance() {
        return instance;
    }

}

