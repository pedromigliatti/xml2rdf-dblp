package eu.wdaqua.dblp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Utility {

    public static final String MDATE = " <mdate> ";
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
    public static final String EDITOR = "";
    public static final String BOOKTITLE = "";
    public static final String ADDRESS = "";
    public static final String MOUTH = "";
    public static final String CDROM = "";
    public static final String PUBLISHER = "";
    public static final String CROSSREF = "";
    public static final String ISBN = "";
    public static final String SERIES = "";
    public static final String SCHOOL = "";
    public static final String CHAPTER = "";
    public static final String PUBLNR = "";




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
