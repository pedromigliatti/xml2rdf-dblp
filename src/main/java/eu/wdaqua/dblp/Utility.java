package eu.wdaqua.dblp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Utility {

    public static final String MDATE = " <http://schema.org/dateModified> ";
    public static final String AUTHOR = " <http://dbpedia.org/ontology/author> ";
    public static final String NAME = " <https://schema.org/name> ";
    public static final String TITLE = " <http://purl.org/dc/elements/1.1/title> ";
    public static final String PAGES = " <http://swrc.ontoware.org/ontology#pages> ";
    public static final String YEAR = " <http://purl.org/dc/terms/issued> ";
    public static final String VOLUME = " <http://swrc.ontoware.org/ontology#volume> ";
    public static final String JOURNAL = " <http://swrc.ontoware.org/ontology#journal> ";
    public static final String NUMBER = " <http://swrc.ontoware.org/ontology#number> ";
    public static final String URL = " <http://purl.org/dc/elements/1.1/identifier> ";
    public static final String URL_WWW = " <http://schema.org/url> ";
    public static final String EE = " <http://www.w3.org/2002/07/owl#sameAs> ";
    public static final String NOTE = " <note> ";
    public static final String CITE = " <cite> ";
    public static final String EDITOR = " <http://schema.org/editor> ";
    public static final String BOOKTITLE = " <booktitle> ";
    public static final String ADDRESS = "<http://schema.org/address>";
    public static final String MONTH = " <http://purl.org/dc/terms/issued> ";
    public static final String CDROM = " <cdrom> ";
    public static final String PUBLISHER = " <http://schema.org/publisher> ";
    public static final String CROSSREF = " <crossref> ";
    public static final String ISBN = " <isbn> ";
    public static final String SERIES = " <series> ";
    public static final String SCHOOL = " <school> ";
    public static final String CHAPTER = " <chapter> ";
    public static final String PUBLNR = " <publnr> ";




    public static void writeStringBuffer(StringBuffer str, String path, boolean next) throws IOException {
        //write contents of StringBuffer to a file
        try (BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(path), next))) {
            //write contents of StringBuffer to a file
            bwr.write(str.toString());
            //flush the stream
            bwr.flush();
            //close the stream
        }
    }

}
