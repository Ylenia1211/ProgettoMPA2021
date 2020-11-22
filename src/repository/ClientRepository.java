package repository;
import model.Client;
import util.WriteCSV;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ClientRepository {
    String CSV_FILE_NAME = "Client";
    String formatToSave =".csv";
    String pathWhereSave = "src/DB/";
    List<String[]> schema;
    WriteCSV output = new WriteCSV();

    public ClientRepository(){ }


    public void createSchema() throws IOException {
        File csvOutputFile = new File(pathWhereSave + CSV_FILE_NAME + formatToSave);
        //Crea schema per il csv di Client
        this.schema= new ArrayList<>();
        schema.add(new String[]{"id", "firstName", "lastName", "address", "city", "telephone", "email"});
        output.convertToCSVOutputDB(csvOutputFile, schema);
    }

    public void add(Client p) throws IOException {
        File file = new File(pathWhereSave+CSV_FILE_NAME+formatToSave);
        if (file.exists()) {
            FileWriter fileWritter = null;
            try {
                fileWritter = new FileWriter(file.getPath(),true);
                BufferedWriter bw = new BufferedWriter(fileWritter);
                bw.write(p.toString()+'\n');
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //solo per testare il corretto funzionamento del salvataggio di Client (poi si elimina)
    public static void main(String[] args) throws IOException {
        ClientRepository repo = new ClientRepository();
        repo.createSchema();

        Client clientTest = new Client("roma", "Rossi", "Via Roma", "Palermo", "3627721863", "alex.rossi@gmail.com");
        repo.add(clientTest);
        Client c2 = new Client("Mario", "Rossi", "Via Roma", "Palermo", "3627721863", "alex.rossi@gmail.com");
        repo.add(c2);
        Client c3 = new Client("Luigi", "Rossi", "Via Roma", "Palermo", "3627721863", "alex.rossi@gmail.com");



    }
}
