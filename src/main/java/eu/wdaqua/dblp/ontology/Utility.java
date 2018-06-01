package eu.wdaqua.dblp.ontology;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.vocabulary.RDF;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

public class Utility {

    public static void writeStringBuffer(StringBuffer str, String path, boolean next) throws IOException {
        //write contents of StringBuffer to a file
        try (BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(path), next))) {
            //write contents of StringBuffer to a file
            bwr.write(str.toString());
            //flush the stream
            bwr.flush();
            //close the stream
            bwr.close();
        }
    }

    public static Triple map(Node subject, String value, Mapping mapping){
//        List<Triple> triples = new ArrayList<Triple>();
        Triple t = null;
                Node predicate;
                Node object;
                switch (mapping.getType()) {
                    case URI:
                        predicate = createURI(mapping.getPropertyUri());
                        if(!value.contains("http"))
                            value = "http://dblp.uni-trier.de/db/" + value;
                        object = createURI(value);
                        t = (new Triple(subject, predicate, object));
                        break;
                    case DATE:
                        predicate = createURI(mapping.getPropertyUri());
                        object = NodeFactory.createLiteral(value, XSDDatatype.XSDdate);
                         t = (new Triple(subject, predicate, object));
                        break;
                    case STRING:
                        predicate = createURI(mapping.getPropertyUri());
                        object = NodeFactory.createLiteral(removeSpecialCharacteres(value), XSDDatatype.XSDstring);
                        t = (new Triple(subject, predicate, object));
                        break;
                    case YEAR:
                        predicate = createURI(mapping.getPropertyUri());
                        object = NodeFactory.createLiteral(value, XSDDatatype.XSDgYear);
                        t = (new Triple(subject, predicate, object));
                        break;
                    case INTEGER:
                        predicate = createURI(mapping.getPropertyUri());
                        object = NodeFactory.createLiteral(value, XSDDatatype.XSDinteger);
                         t = (new Triple(subject, predicate, object));
                        break;
                    default:
                        System.out.println("Type not supported " + mapping.getType());
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

        s = removeSpecialCharacteres(s);

        if(!s.contains("http"))
            s = "http://dblp.uni-trier.de/" + s;


        return NodeFactory.createURI(s);
    }

    public static String removeSpecialCharacteres(String s){
        if (s.contains("\\|")){
            System.out.println("This URI "+s+"contains illigal caracter |");
            s = s.replace("\\|","");
        }
        if (s.contains("|")){
            //System.out.println("This URI "+s+"contains illigal caracter |");
            s = s.replace("|","\u0007C");
        }
        return s;
    }

    //method to write all classes and properties to define the schema
    public static void writeVocabulary(StreamRDF writer) throws IOException {
            for (Mapping m : Classes.getMappings()) {
                Node subject = createURI(m.getPropertyUri());
                Node object = createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#Classes");
                Triple t = new Triple(subject, RDF.type.asNode(),object);
                writer.triple(t);
            }

            for (Mapping m : Properties.getMappings()) {
                Node subject = createURI(m.getPropertyUri());
                Node object = createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#Property");
                Triple t = new Triple(subject, RDF.type.asNode(), object);
                writer.triple(t);
            }
    }
}
