package repository;
import javafx.collections.ObservableList;
import model.Client;
import util.WriteCSV;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ClientRepository {
    String CSV_FILE_NAME = "Client";
    String formatToSave =".csv";
    String pathWhereSave = "src/DB/";
    List<String[]> schema;
    WriteCSV output = new WriteCSV();

    public ClientRepository(){
        try {
            if(!(new File(pathWhereSave, CSV_FILE_NAME+formatToSave).exists())){
                //boolean check = !(new File(pathWhereSave, CSV_FILE_NAME+formatToSave).exists());
                //System.out.println(check);
                this.createSchema();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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

    public List<String> findAll() throws IOException {
         return Files.lines(Paths.get(pathWhereSave+CSV_FILE_NAME+formatToSave))
                .skip(1) // ignore the first entry (scherma)
                 .collect(Collectors.toList());
    }

    public List<String> findByString(String input) throws IOException {  //ricerca ogni riga nella tabella che contiene la stringa
        return Files.lines(Paths.get(pathWhereSave+CSV_FILE_NAME+formatToSave))
                .skip(1) // ignore the first entry (scherma)
                .filter(line -> line.contains(input))   //esempio
                .collect(Collectors.toList());
    }
    public void deleteItem(int index) throws IOException {
        Stream<String> reader2 = Files.lines(Paths.get(pathWhereSave+CSV_FILE_NAME+formatToSave));
        //List<String[]> allElements = reader2;
        //allElements.remove(rowNumber);



    }
    //solo per testare il corretto funzionamento del salvataggio di Client (poi si elimina)
    public static void main(String[] args) throws IOException {
        ClientRepository repo = new ClientRepository();

        Client clientTest = new Client("roma", "Rossi", "Via Roma", "Palermo", "3627721863", "alex.rossi@gmail.com");
        repo.add(clientTest);
        Client c2 = new Client("Mario", "Rossi", "Via Roma", "Palermo", "3627721863", "alex.rossi@gmail.com");
        repo.add(c2);
        Client c3 = new Client("Luigi", "Rossi", "Via Roma", "Palermo", "3627721863", "alex.rossi@gmail.com");
        List<String> csvTableClient = repo.findAll();
        csvTableClient.forEach(System.out::println);

        //per usare le seguenti linee bisogna prima implementare un metodo findByName (deve prendere solo le righe nella tabella che siano corrispondenti al nome passato a parametro)
        //bisogna filtrare per colonne
        /*
        System.out.println("Ricerca per nome es.\"mario\": ");
        Scanner sc= new Scanner(System.in);
        System.out.print("Inserisci un nome: ");
        String inputName = sc.nextLine();
        repo.findByName(inputName).forEach(System.out::println);
        */
    }


}
