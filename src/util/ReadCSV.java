package util;

public class ReadCSV {

    private String path;
    private String format;
    private String file;


    public ReadCSV(){
        WriteCSV w = new WriteCSV();
        this.path = w.getPathWhereSave();
        this.format =w.getFormatToSave();
        this.file = w.getCSV_FILE_NAME();
    }

    public ReadCSV(String path, String format, String file) {
        this.path = path;
        this.format = format;
        this.file = file;
    }

}
