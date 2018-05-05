package eu.wdaqua.dblp.ontology;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Classes {
    public static final String article = "http://swrc.ontoware.org/ontology#Article";
    public static final String proceedings = "http://purl.org/ontology/bibo/Proceedings";
    public static final String inproceedings = "http://swrc.ontoware.org/ontology#InProceedings";
    public static final String incollection = "http://swrc.ontoware.org/ontology#InCollection";
    public static final String book = "http://swrc.ontoware.org/ontology#Book";
    public static final String phdthesis = "http://swrc.ontoware.org/ontology#PhDThesis";
    public static final String mastersthesis = "http://sw-portal.deri.org/ontologies/swportal#MasterThesis";
    public static final String www = "http://xmlns.com/foaf/0.1/Person";

    public static Map<String,String> getFields() {
        try {
            Map<String, String> classes = new HashMap<String, String>();
            for (Field f : Classes.class.getDeclaredFields()) {
                classes.put(f.getName(), f.get(Classes.class).toString());
            }
            return classes;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
