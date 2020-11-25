package util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WriteCSV {


    private String CSV_FILE_NAME = "TEST.csv";
    private String formatToSave =".csv";
    private String pathWhereSave = "src/DB/";

    public WriteCSV(){  }   //empty costructor

    public WriteCSV(String CSV_FILE_NAME, String formatToSave, String pathWhereSave) {
        this.CSV_FILE_NAME = CSV_FILE_NAME;
        this.formatToSave = formatToSave;
        this.pathWhereSave = pathWhereSave;
    }

    public String getCSV_FILE_NAME() {
        return CSV_FILE_NAME;
    }

    public String getFormatToSave() {
        return formatToSave;
    }

    public String getPathWhereSave() {
        return pathWhereSave;
    }




    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    public void convertToCSVOutputDB(File csvOutputFile, List<String[]> dataLines) throws IOException {
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
        csvOutputFile.exists();
    }

    /*
    public void convertToCSVOutput(List<String[]> dataLines, String pathWhereSave, String CSV_FILE_NAME, String formatToSave) throws IOException {
        File csvOutputFile = new File(pathWhereSave + CSV_FILE_NAME + formatToSave);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
        csvOutputFile.exists();
    }


    public static void main(String[] args) throws IOException {
        WriteCSV test = new WriteCSV();
        List<String[]> dataLines = new ArrayList<>();
        dataLines.add(new String[]
                {"John", "Doe", "38", "Comment Data\nAnother line of comment data"});
        dataLines.add(new String[]
                {"Jane", "Doe, Jr.", "19", "She said \"I'm being quoted\""});
        test.convertToCSVOutput(dataLines, "src/DB/", "test", ".csv" );
    }*/
}
