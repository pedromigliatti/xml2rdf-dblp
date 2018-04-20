package eu.wdaqua.dblp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public static final String NOTE = " <http://www.w3.org/2004/02/skos/core#note> ";
    public static final String CITE = " <http://www.w3.org/1999/xhtml/vocab#cite> ";
    public static final String EDITOR = " <http://schema.org/editor> ";
    public static final String BOOKTITLE = " <http://lsdis.cs.uga.edu/projects/semdis/opus#book_title> "; //" <booktitle> "; //I could not map
    public static final String ADDRESS = "<http://schema.org/address>";
    public static final String MONTH = " <http://purl.org/dc/terms/issued> ";
    public static final String CDROM = " <http://lsdis.cs.uga.edu/projects/semdis/opus#cdrom> ";
    public static final String PUBLISHER = " <http://schema.org/publisher> ";
    public static final String CROSSREF = " <http://data.europa.eu/eli/ontology#published_in> "; //" <http://purl.org/net/nknouf/ns/bibtex#hasCrossref> ";
    public static final String ISBN = " <http://schema.org/isbn> ";
    public static final String SERIES = " <http://lsdis.cs.uga.edu/projects/semdis/opus#in_series> ";
    public static final String SCHOOL = " <http://lsdis.cs.uga.edu/projects/semdis/opus#School> ";
    public static final String CHAPTER = " <http://lsdis.cs.uga.edu/projects/semdis/opus#chapter> ";
    public static final String PUBLNR = " <http://wdaqua.eu/publnr> "; //I could not map

    public static final String ARTICLE = "<http://swrc.ontoware.org/ontology#Article> ";
    public static final String PROCEEDINGS = "<http://purl.org/ontology/bibo/Proceedings> ";
    public static final String INPROCEEDINGS = "<http://swrc.ontoware.org/ontology#InProceedings> ";
    public static final String INCOLLECTION = "<http://swrc.ontoware.org/ontology#InCollection> ";
    public static final String BOOK = "<http://swrc.ontoware.org/ontology#Book> ";
    public static final String PHDTHESIS = "<http://swrc.ontoware.org/ontology#PhDThesis> ";
    public static final String MASTERSTHESIS = "<http://sw-portal.deri.org/ontologies/swportal#MasterThesis> ";
    public static final String WWW = "<http://xmlns.com/foaf/0.1/Person> ";





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
