package util.pdfutilities;

import j2html.tags.ContainerTag;
import j2html.tags.Tag;

import static j2html.TagCreator.*;

/**
 * @author Ylenia Galluzzo
 * @author Matia Fazio
 * @version 1.0
 * @since 1.0
 * <p>
 * Classe utilizzata per creare un oggetto 'HTMLCreator':{@link HTMLCreator}
 * il quale contiene i metodi utili per la creazione di un template e struttura di un file HTML.
 */
public class HTMLCreator {

    /**
     * Metodo che crea la struttura di un file HTML.
     *
     * @param pageTitle titolo da assegnare alla pagina Html
     * @param tags      tags da inserire all'interno della struttura HTML
     * @return un documento HTML
     */
    public String createHtml(String pageTitle, Tag... tags) {
        ContainerTag html = html(
                head
                        (title(pageTitle), style("""
                                h1, h2, h3 {
                                    text-align:center;
                                    color: #3DA4E3;
                                    font-family: "Calibri",serif;
                                }
                                p {
                                 text-align: left;
                                }
                                table {
                                    border-collapse: separate;
                                    border: 1px solid #3DA4E3;
                                    vertical-align: top;
                                    font-size: 10;
                                }
                                tr {
                                    vertical-align: top;
                                    border-collapse: collapse;
                                    border: 1px solid #3DA4E3;
                                }""")
                        ),
                //link().withRel("stylesheet").withHref(pathCss).withType("text/css")),
                body(
                        header(),
                        main(tags), //contiene tutti i tag dell'html
                        footer()
                )
        ).attr("lang", "it");
        return document().render() + html.render();
    }

}
