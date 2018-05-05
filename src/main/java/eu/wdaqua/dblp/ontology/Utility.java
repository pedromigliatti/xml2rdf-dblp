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

    public static List<Triple> map(Node subject, String tag, String value){
        List<Triple> triples = new ArrayList<Triple>();
        for (PropertyMapping mapping : Properties.getMappings()) {
            if (mapping.getTag().equals(tag)) {
                Node predicate;
                Node object;
                switch (mapping.getType()) {
                    case URI:
                        predicate = createURI(mapping.getPropertyUri());
                        object = createURI(value);
                        triples.add(new Triple(subject, predicate, object));
                        break;
                    case DATE:
                        predicate = createURI(mapping.getPropertyUri());
                        object = NodeFactory.createLiteral(value, XSDDatatype.XSDdate);
                        triples.add(new Triple(subject, predicate, object));
                        break;
                    case STRING:
                        predicate = createURI(mapping.getPropertyUri());
                        object = NodeFactory.createLiteral(value, XSDDatatype.XSDstring);
                        triples.add(new Triple(subject, predicate, object));
                        break;
                    case YEAR:
                        predicate = createURI(mapping.getPropertyUri());
                        object = NodeFactory.createLiteral(value, XSDDatatype.XSDgYear);
                        triples.add(new Triple(subject, predicate, object));
                        break;
                    case INTEGER:
                        predicate = createURI(mapping.getPropertyUri());
                        object = NodeFactory.createLiteral(value, XSDDatatype.XSDinteger);
                        triples.add(new Triple(subject, predicate, object));
                        break;
                    default:
                        System.out.println("Type not supported " + mapping.getType());
                }
            }
        }
        return triples;
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
        if (s.contains("\\|")){
            System.out.println("This URI "+s+"contains illigal caracter |");
            s = s.replace("\\|","");
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
        return NodeFactory.createURI(s);
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
