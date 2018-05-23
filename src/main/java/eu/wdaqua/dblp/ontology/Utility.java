package eu.wdaqua.dblp.ontology;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.vocabulary.RDF;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static Triple map(Node subject, String value, PropertyMapping propertyMapping){
//        List<Triple> triples = new ArrayList<Triple>();
        Triple t = null;
        for (PropertyMapping mapping : Properties.getMappings()) {
            if (mapping.getTag().equals(propertyMapping.getTag()) && mapping.getType().equals(propertyMapping.getType())) {
                Node predicate;
                Node object;
                switch (mapping.getType()) {
                    case URI:
                        predicate = createURI(propertyMapping.getPropertyUri());
                        object = createURI(value);
                        t = (new Triple(subject, predicate, object));
                        break;
                    case DATE:
                        predicate = createURI(propertyMapping.getPropertyUri());
                        object = NodeFactory.createLiteral(value, XSDDatatype.XSDdate);
                         t = (new Triple(subject, predicate, object));
                        break;
                    case STRING:
                        predicate = createURI(propertyMapping.getPropertyUri());
                        object = NodeFactory.createLiteral(removeSpecialCharacteres(value), XSDDatatype.XSDstring);
                        t = (new Triple(subject, predicate, object));
                        break;
                    case YEAR:
                        predicate = createURI(propertyMapping.getPropertyUri());
                        object = NodeFactory.createLiteral(value, XSDDatatype.XSDgYear);
                        t = (new Triple(subject, predicate, object));
                        break;
                    case INTEGER:
                        predicate = createURI(propertyMapping.getPropertyUri());
                        object = NodeFactory.createLiteral(value, XSDDatatype.XSDinteger);
                         t = (new Triple(subject, predicate, object));
                        break;
                    default:
                        System.out.println("Type not supported " + mapping.getType());
                }
            }
        }
        return t;
    }

    public static Node createURI(String s){
        if (s.contains(">")){
            System.out.println("This URI "+s+"contains illigal caracter >");
            s = s.replace(">","");
        }
        if (s.contains("<")){
            System.out.println("This URI "+s+"contains illigal caracter <");
            s = s.replace("<","");
        }
        if (s.contains("\\")){
            System.out.println("This URI "+s+"contains illigal caracter \\");
            s = s.replace("\\","");
        }
        if (s.contains("}")){
            System.out.println("This URI "+s+"contains illigal caracter }");
            s = s.replace("}","");
        }
        if (s.contains("{")){
            System.out.println("This URI "+s+"contains illigal caracter {");
            s = s.replace("{","");
        }
        if (s.contains("\"")){
            System.out.println("This URI "+s+"contains illigal caracter \"");
            s = s.replace("\"","");
        }
        return NodeFactory.createURI(removeSpecialCharacteres(s));
    }

    public static String removeSpecialCharacteres(String s){
        if (s.contains("\\|")){
            System.out.println("This URI "+s+"contains illigal caracter |");
            s = s.replace("\\|","");
        }
        if (s.contains("|")){
            System.out.println("This URI "+s+"contains illigal caracter |");
            s = s.replace("|","");
        }
        return s;
    }

    //method to write all classes and properties to define the schema
    public static void writeVocabulary(StreamRDF writer) throws IOException {
        try {
            for (Field f : Classes.class.getDeclaredFields()) {
                Node subject = createURI(f.get(Classes.class).toString());
                Node object = createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#Classes");
                Triple t = new Triple(subject, RDF.type.asNode(),object);
                writer.triple(t);
            }

            for (PropertyMapping m : Properties.getMappings()) {
                Node subject = createURI(m.getPropertyUri());
                Node object = createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#Property");
                Triple t = new Triple(subject, RDF.type.asNode(), object);
                writer.triple(t);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
