package util.pdfutilities;

import j2html.tags.ContainerTag;
import j2html.tags.Tag;

import static j2html.TagCreator.*;

public class HTMLcreator {

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
