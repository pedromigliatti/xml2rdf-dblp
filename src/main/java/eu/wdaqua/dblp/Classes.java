package eu.wdaqua.dblp;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Classes {
    public static final String ARTICLE = "<http://swrc.ontoware.org/ontology#Article> ";
    public static final String PROCEEDINGS = "<http://purl.org/ontology/bibo/Proceedings> ";
    public static final String INPROCEEDINGS = "<http://swrc.ontoware.org/ontology#InProceedings> ";
    public static final String INCOLLECTION = "<http://swrc.ontoware.org/ontology#InCollection> ";
    public static final String BOOK = "<http://swrc.ontoware.org/ontology#Book> ";
    public static final String PHDTHESIS = "<http://swrc.ontoware.org/ontology#PhDThesis> ";
    public static final String MASTERSTHESIS = "<http://sw-portal.deri.org/ontologies/swportal#MasterThesis> ";
    public static final String WWW = "<http://xmlns.com/foaf/0.1/Person> ";

    public static Map<String,String> getFields() throws IllegalAccessException {
        Map<String, String> classes = new HashMap<>();
        for (Field f : Classes.class.getDeclaredFields()) {
            classes.put(f.getName(), f.get(Classes.class).toString());
        }
        return classes;
    }

}
