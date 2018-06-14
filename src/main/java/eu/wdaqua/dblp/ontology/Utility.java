package eu.wdaqua.dblp.ontology;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.vocabulary.RDF;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

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
        s = s.replace(" ","_");
        s = removeSpecialCharacteres(s);

        if(!s.contains("http"))
            s = "http://dblp.uni-trier.de/" + s;


        return NodeFactory.createURI(s);
    }

    public static String removeSpecialCharacteres(String s){
        if (s.contains(">")){
            System.out.println("This URI "+s+"contains illigal caracter >");
            //s = s.replace(">","\\u003E");
            s = s.replace(">","");
        }
        if (s.contains("<")){
            System.out.println("This URI "+s+"contains illigal caracter <");
            //s = s.replace("<","\\u003C");
            s = s.replace("<","");
        }
        if (s.contains("\\")){
            System.out.println("This URI "+s+"contains illigal caracter \\");
            //s = s.replace("\\","\\u005C");
            s = s.replace("\\","");
        }
        if (s.contains("}")){
            System.out.println("This URI "+s+"contains illigal caracter }");
            //s = s.replace("}","\\u007D");
            s = s.replace("}","");
        }
        if (s.contains("{")){
            System.out.println("This URI "+s+"contains illigal caracter {");
            //s = s.replace("{","\\u007B");
            s = s.replace("{","");
        }
        if (s.contains("\"")){
            System.out.println("This URI "+s+"contains illigal caracter \"");
            //s = s.replace("\"","\\u0022");
            s = s.replace("\"","");
        }
        if (s.contains("|")){
            System.out.println("This URI "+s+"contains illigal caracter |");
            //s = s.replace("|","\\u0007C");
            s = s.replace("|","");
        }
        if (s.contains("\\|")){
            System.out.println("This URI "+s+"contains illigal caracter |");
            //s = s.replace("\\|","");
            s = s.replace("\\|","");
        }
        if (s.contains("|")){
            System.out.println("This URI "+s+"contains illigal caracter |");
            //s = s.replace("|","\\u0007C");
            s = s.replace("|","");
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

    public static Map<String,String> readMonthJson(String path){
        JSONParser parser = new JSONParser();

        Map<String, String> months = new HashMap<>();

        try{
            Object obj = parser.parse(new FileReader(path));

            JSONArray jsonArray = (JSONArray) obj;

            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;

                String uri = jsonObject.get("m_s").toString();
                String month = jsonObject.get("m_sLabel").toString();

                months.put(month, uri);
            }


        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return months;
    }
}
