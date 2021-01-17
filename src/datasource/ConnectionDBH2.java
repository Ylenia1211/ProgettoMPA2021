package datasource;

import dao.ConcreteOwnerDAO;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDBH2 {
    public String name_db = "dataBaseH2";
    //private String url = "jdbc:h2:~/test" + ";IFEXISTS=TRUE";
    private String url = "jdbc:h2:./h2/bin/database/"+ name_db +";IFEXISTS=TRUE";
    private String username = "";
    private String password = "";
    //static Connection connectData = null;
    private static Connection connectData = null;

    //SINGLETON
    private static final ConnectionDBH2 instance = new ConnectionDBH2();
    //SINGLETON ha il costruttore privato
    private ConnectionDBH2(){
        try {
            Class.forName("org.h2.Driver");
            connectData = DriverManager.getConnection(url, username, password);
            System.out.println("DB connected!");
           // return  connectData;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
           // return null;
        }
    }
   //metodo per prendere l'instanza della connessione univoca per tutto il programma
    public Connection getConnectData() {
        return connectData;
    }

    public static ConnectionDBH2 getInstance(){
        return instance;
    }

    /*
    public Connection dbConnection(){
        try {
            Class.forName("org.h2.Driver");
            connectData = DriverManager.getConnection(url, username, password);
            System.out.println("DB connected!");
            return  connectData;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
            return null;
        }
    }

    public static void quitConnectionDB(){
        try {
            connectData.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    */

}

