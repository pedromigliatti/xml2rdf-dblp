package eu.wdaqua.dblp.ontology;

import org.apache.commons.lang3.ObjectUtils;

import java.lang.reflect.Field;
import java.util.*;

public class Classes {
    public static final ArrayList<Mapping> mappings = new ArrayList<Mapping>(Arrays.asList(
            new Mapping("article","http://swrc.ontoware.org/ontology#Article",Type.CLASS),
            new Mapping("article","http://www.eurocris.org/ontologies/cerif/1.3#Publication",Type.CLASS),
            new Mapping("proceedings","http://purl.org/ontology/bibo/Proceedings",Type.CLASS),
            new Mapping("inproceedings","http://swrc.ontoware.org/ontology#InProceedings",Type.CLASS),
            new Mapping("inproceedings","http://www.eurocris.org/ontologies/cerif/1.3#Publication",Type.CLASS),
            new Mapping("incollection","http://swrc.ontoware.org/ontology#InCollection",Type.CLASS),
            new Mapping("book","http://swrc.ontoware.org/ontology#Book",Type.CLASS),
            new Mapping("phdthesis","http://swrc.ontoware.org/ontology#PhDThesis",Type.CLASS),
            new Mapping("phdthesis","http://www.eurocris.org/ontologies/cerif/1.3#Publication",Type.CLASS),
            new Mapping("mastersthesis","http://sw-portal.deri.org/ontologies/swportal#MasterThesis",Type.CLASS),
            new Mapping("mastersthesis","http://www.eurocris.org/ontologies/cerif/1.3#Publication",Type.CLASS),
            new Mapping("www","http://xmlns.com/foaf/0.1/Person",Type.CLASS),
            new Mapping("journal","http://xmlns.com/foaf/0.1/Person",Type.CUSTOM)
    ));

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

    public static List<Mapping> getMappings(){
        return mappings;
    }

    public static Map<String,ObjectUtils.Null> getMappedTags(){
        Map<String,ObjectUtils.Null> map = new HashMap<String,ObjectUtils.Null>();
        for (Mapping c : mappings){
            map.put(c.getTag(),null);
        }
        return map;
    }

    public static List<Mapping> getMapping(String tag){
        List<Mapping> classes = new ArrayList<Mapping>();
        for (Mapping m : mappings){
            if (m.getTag().equals(tag)){
                classes.add(m);
            }
        }

        return classes;
    }

}
