package util.pdfutilities;

import model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static j2html.TagCreator.*;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per creare un oggetto 'HTMLReport':{@link HTMLReport}
 * il quale contiene i metodi utili per la creazione di un report in HTML.
 */
public class HTMLReport {
    private final HTMLCreator htmLcreator = new HTMLCreator();

    /**
     * Metodo che crea la pagina HTML con i tags.
     *
     * @param report      oggetto report {@link Report} di cui vogliamo creare il PDF.
     * @param appointment oggetto Appointment che contiene dati utili della visita da immagazinare nel PDF del Report
     * @param owner       oggetto Owner che contiene dati utili della visita da immagazinare nel PDF del Report
     * @param pet         oggetto Pet che contiene dati utili della visita da immagazinare nel PDF del Report.
     * @param doctor      oggetto Doctor che contiene dati utili della visita da immagazinare nel PDF del Report
     * @return un documento HTML
     */
    public String generatePageHtml(Report report, Appointment appointment, Owner owner, Pet pet, Doctor doctor) {
        return htmLcreator.createHtml("Report Page",
                div().with(
                        h1("Vet Clinic Management Report")
                ),
                div().with(
                        table().attr("style", "border: 1px solid transparent;").with(
                                tr().with(
                                        td().with(
                                                table().with(
                                                        tr().with(
                                                                td().attr("colspan", "2")
                                                                        .attr("style", "text-align: center; background-color: #add8e6; font-size: 12;")
                                                                        .with(
                                                                                span("Dati Cliente")
                                                                        )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Cognome")
                                                                        ),
                                                                td().with(
                                                                        span(owner.getSurname())
                                                                )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Nome")
                                                                        ),
                                                                td().with(
                                                                        span(owner.getName())
                                                                )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Codice Fiscale")
                                                                        ),
                                                                td().with(
                                                                        span(owner.getFiscalCode())
                                                                )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Data di Nascita")
                                                                        ),
                                                                td().with(
                                                                        span(owner.getDatebirth().toString())
                                                                )
                                                        )
                                                )
                                        ),
                                        td().with(
                                                table().with(
                                                        tr().with(
                                                                td().attr("colspan", "2")
                                                                        .attr("style", "text-align: center; background-color: #add8e6; font-size: 12;")
                                                                        .with(
                                                                                span("Dati Paziente")
                                                                        )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Cognome")
                                                                        ),
                                                                td().with(
                                                                        span(pet.getSurname())
                                                                )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Nome")
                                                                        ),
                                                                td().with(
                                                                        span(pet.getName())
                                                                )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Sesso")
                                                                        ),
                                                                td().with(
                                                                        span(pet.getSex().toString())
                                                                )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Data di Nascita")
                                                                        ),
                                                                td().with(
                                                                        span(pet.getDatebirth().toString())
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                ),
                div().with(
                        table().attr("style", "border: 1px solid transparent;").with(
                                tr().with(
                                        td().with(
                                                table().with(
                                                        tr().with(
                                                                td().attr("colspan", "2")
                                                                        .attr("style", "text-align: center; background-color: #add8e6; font-size: 12;")
                                                                        .with(
                                                                                span("Dati Prenotazione")
                                                                        )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Tipologia di visita")
                                                                        ),
                                                                td().with(
                                                                        span(appointment.getSpecialitation())
                                                                )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Data")
                                                                        ),
                                                                td().with(
                                                                        span(appointment.getLocalDate().toString())
                                                                )
                                                        )
                                                )
                                        ),
                                        td().with(
                                                table().with(
                                                        tr().with(
                                                                td().attr("colspan", "2")
                                                                        .attr("style", "text-align: center; background-color: #add8e6; font-size: 12;")
                                                                        .with(
                                                                                span("Dati Medico")
                                                                        )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Cognome")
                                                                        ),
                                                                td().with(
                                                                        span(doctor.getSurname())
                                                                )
                                                        ),
                                                        tr().with(
                                                                td().attr("style", "background-color: #a7c4c9")
                                                                        .with(
                                                                                span("Nome")
                                                                        ),
                                                                td().with(
                                                                        span(doctor.getName())
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                ),
                div().with(
                        h2().with(
                                span("Diagnosi")
                        ),
                        p().with(
                                span(report.getDiagnosis())
                        )
                ),
                div().with(
                        h2().with(
                                span("Terapia")
                        ),
                        p().with(
                                span(report.getTreatments())
                        )
                )
        );
    }

    /**
     * Metodo che genera il file HTML del Report.
     * Richiama il metodo generatePageHTML() di {@link HTMLReport}
     *
     * @param report      oggetto report {@link Report} di cui vogliamo creare il PDF.
     * @param appointment oggetto Appointment che contiene dati utili della visita da immagazinare nel PDF del Report
     * @param owner       oggetto Owner che contiene dati utili della visita da immagazinare nel PDF del Report
     * @param pet         oggetto Pet che contiene dati utili della visita da immagazinare nel PDF del Report.
     * @param doctor      oggetto Doctor che contiene dati utili della visita da immagazinare nel PDF del Report
     * @return il path dove è stato salvato il file Html del report.
     */
    public String savePageHtml(Report report, Appointment appointment, Owner owner, Pet pet, Doctor doctor) throws IOException {
        FileWriter fWriter;
        BufferedWriter writer;
        String inputFile = "./fileName.html";
        fWriter = new FileWriter(inputFile);
        writer = new BufferedWriter(fWriter);
        writer.write(generatePageHtml(report, appointment, owner, pet, doctor));
        writer.newLine(); //this is not actually needed for html files - can make your code more readable though
        writer.close(); //make sure you close the writer object
        return inputFile;
    }

    /**
     * Metodo che cancella il file HTML del Report.
     *
     * @param inputFile path del file html generato nella prima fase di costruzione del file per il report.
     */
    public void deletePageHtml(String inputFile) {

        //per cancellare il file html di supporto creato visto che non ci serve
        File file = new File(inputFile);
        if (file.delete()) {
            System.out.println("File html cancellato");
        } else {
            System.out.println("File NON cancellato");
        }

    }

}
