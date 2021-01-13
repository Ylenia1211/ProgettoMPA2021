package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReportViewController implements Initializable {
    public TextArea textDiagnosi;
    public TextArea textTerapia;
    public TextField textPath;
    public ImageView attachmentImage;
    public ArrayList<String> attachments = new ArrayList<>();
    public Label firstAttachment;
    public VBox allegati;
    public Button creaReportButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.attachmentImage.setStyle("-fx-background-image: url('/resources/attachment.png')");
        this.creaReportButton.setOnAction(actionEvent -> {
            try {
                creaReport(this.textDiagnosi.getText(),
                           this.textTerapia.getText(),
                           this.attachments);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void findAttachment() {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            String filePath = file.getAbsolutePath();
            if (this.firstAttachment.getText().equals("Nessuno"))
                this.firstAttachment.setText(filePath);
            else
                this.allegati.getChildren().add(new Label(filePath));
            this.attachments.add(filePath);
            this.textPath.setText(filePath);
        }
    }

    public void creaReport(String diagnosi, String terapia, ArrayList<String> attachments) throws IOException {
        //int ID = ++DoctorDashboard.reportID;
        //Creating PDF document object
        PDDocument document = new PDDocument();
        PDPage my_page = new PDPage();
        PDPageContentStream contentStream = new PDPageContentStream(document, my_page);
        //Begin the Content stream
        contentStream.beginText();

        //Setting the font to the Content stream
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
        contentStream.setLeading(14.5f);
        //Setting the position for the line
        contentStream.newLineAtOffset(25, 700);

        String text = String.format("""
                Paziente:
                Dati cane
                                        
                Diagnosi
                %s
                                        
                Terapia
                %s
                                        
                Allegati:
                """, this.textDiagnosi, this.textTerapia);
//        if (this.attachments.size() > 0){
//            for (String path: this.attachments) {
//                PDImageXObject pdImage = PDImageXObject.createFromFile(path, document);
//                contentStream = new PDPageContentStream(document, my_page);
//                contentStream.drawImage(pdImage, 70, 250);
//            }
//
//        }

        //Adding text in the form of string
        contentStream.showText(text);

        //Ending the content stream
        contentStream.endText();

        System.out.println("Content added");

        //Closing the content stream
        contentStream.close();
        document.addPage(my_page);
        //Saving the document
        String currentPath = System.getProperty("user.dir");
        document.save(currentPath + "/report/report_test.pdf");

        System.out.println("PDF created");

        //Closing the document
        document.close();
    }


}
